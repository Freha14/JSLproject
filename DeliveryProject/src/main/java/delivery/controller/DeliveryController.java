package delivery.controller;

import delivery.dao.DeliveryDao;
import delivery.dto.DeliveryDto;
import delivery.dto.DeliveryProductDto;
import delivery.service.DeliveryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/delivery/*")
public class DeliveryController extends HttpServlet {
    private final DeliveryDao dao = new DeliveryDao();
    private final DeliveryService service = new DeliveryService();

    private String memberId(HttpServletRequest req) {
        Object value = req.getSession(false) == null ? null : req.getSession(false).getAttribute("memberId");
        return value == null ? null : String.valueOf(value);
    }
    private boolean loggedIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (memberId(req) == null || memberId(req).isBlank()) {
            resp.sendRedirect(req.getContextPath()+"/login.jsp"); return false;
        }
        return true;
    }
    private String s(HttpServletRequest req, String name) { String v=req.getParameter(name); return v==null?null:v.trim(); }
    private void setEncoding(HttpServletRequest req) throws IOException { req.setCharacterEncoding("UTF-8"); }

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        setEncoding(req); if(!loggedIn(req,resp)) return;
        String path=req.getPathInfo()==null?"/list":req.getPathInfo();
        try {
            if("/list".equals(path)) {
                req.setAttribute("deliveries",dao.findByMember(memberId(req)));
                req.getRequestDispatcher("/delivery/deliveryList.jsp").forward(req,resp);
            } else if("/new".equals(path)) {
                req.getRequestDispatcher("/delivery/deliveryForm.jsp").forward(req,resp);
            } else if("/detail".equals(path) || "/edit".equals(path)) {
                long id=parseId(s(req,"id"));
                DeliveryDto dto=dao.findDetail(id,memberId(req));
                if(dto==null) { resp.sendError(404,"신청서를 찾을 수 없습니다."); return; }
                req.setAttribute("delivery",dto);
                req.getRequestDispatcher("/delivery"+("/edit".equals(path)?"/deliveryEdit.jsp":"/deliveryDetail.jsp")).forward(req,resp);
            } else { resp.sendError(404); }
        } catch(NumberFormatException e) { resp.sendError(400,"잘못된 신청번호입니다."); }
          catch(SQLException e) { throw new ServletException("배송신청 조회 중 오류",e); }
    }

    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        setEncoding(req); if(!loggedIn(req,resp)) return;
        String path=req.getPathInfo();
        try {
            if("/save".equals(path)) {
                DeliveryDto dto=readForm(req); validate(dto);
                dto.setMemberId(memberId(req));
                long id=service.create(dto);
                resp.sendRedirect(req.getContextPath()+"/delivery/detail?id="+id);
            } else if("/update".equals(path)) {
                DeliveryDto dto=readForm(req); dto.setRequestId(parseId(s(req,"requestId"))); validate(dto);
                if(!service.update(dto,memberId(req))) { resp.sendError(409,"수정할 수 없는 신청서입니다."); return; }
                resp.sendRedirect(req.getContextPath()+"/delivery/detail?id="+dto.getRequestId());
            } else if("/cancel".equals(path)) {
                long id=parseId(s(req,"requestId"));
                if(!dao.cancel(id,memberId(req))) { resp.sendError(409,"취소할 수 없는 신청서입니다."); return; }
                resp.sendRedirect(req.getContextPath()+"/delivery/list");
            } else { resp.sendError(404); }
        } catch(NumberFormatException e) {
            resp.sendError(400,"잘못된 신청번호 또는 상품 정보입니다.");
        } catch(IllegalArgumentException e) {
            req.setAttribute("error",e.getMessage());
            req.setAttribute("delivery",safeReadForm(req));
            req.getRequestDispatcher("/delivery/deliveryForm.jsp").forward(req,resp);
        } catch(SQLException e) { throw new ServletException("배송신청 저장 중 오류",e); }
    }

    private DeliveryDto safeReadForm(HttpServletRequest req) {
        try { return readForm(req); } catch(Exception e) { return new DeliveryDto(); }
    }
    private DeliveryDto readForm(HttpServletRequest req) {
        DeliveryDto d=new DeliveryDto();
        d.setCenterCode(s(req,"centerCode")); d.setOrderNumber(s(req,"orderNumber"));
        d.setRecipientName(s(req,"recipientName")); d.setRecipientPhone(s(req,"recipientPhone"));
        d.setRecipientPostcode(s(req,"recipientPostcode")); d.setRecipientAddress(s(req,"recipientAddress"));
        d.setRequestMemo(s(req,"requestMemo"));
        String[] names=req.getParameterValues("productName");
        String[] urls=req.getParameterValues("productUrl");
        String[] qtys=req.getParameterValues("quantity");
        String[] prices=req.getParameterValues("unitPrice");
        String[] cats=req.getParameterValues("productCategory");
        List<DeliveryProductDto> products=new ArrayList<>();
        if(names!=null) for(int i=0;i<names.length;i++) {
            String name=names[i]==null?"":names[i].trim();
            String qty=at(qtys,i), price=at(prices,i);
            if(name.isEmpty() && qty.isEmpty() && price.isEmpty()) continue;
            DeliveryProductDto p=new DeliveryProductDto();
            p.setProductName(name); p.setProductUrl(at(urls,i)); p.setProductCategory(at(cats,i));
            p.setQuantity(Integer.parseInt(qty)); p.setUnitPrice(new BigDecimal(price)); products.add(p);
        }
        d.setProducts(products); return d;
    }
    private String at(String[] a,int i) { return a==null||i>=a.length||a[i]==null?"":a[i].trim(); }
    private void validate(DeliveryDto d) {
        if(blank(d.getCenterCode())||blank(d.getRecipientName())||blank(d.getRecipientPhone())||blank(d.getRecipientAddress()))
            throw new IllegalArgumentException("배송센터, 수령인, 연락처, 주소는 필수입니다.");
        if(d.getProducts()==null||d.getProducts().isEmpty()) throw new IllegalArgumentException("상품을 한 개 이상 입력해 주세요.");
        for(DeliveryProductDto p:d.getProducts()) {
            if(blank(p.getProductName())||p.getQuantity()<1||p.getUnitPrice()==null||p.getUnitPrice().compareTo(BigDecimal.ZERO)<0)
                throw new IllegalArgumentException("상품명, 1개 이상의 수량, 0 이상의 단가를 확인해 주세요.");
            if(p.getProductName().length()>200||p.getProductUrl().length()>1000||p.getProductCategory().length()>100)
                throw new IllegalArgumentException("상품 정보가 허용 길이를 초과했습니다.");
        }
    }
    private boolean blank(String s) { return s==null||s.isBlank(); }
    private long parseId(String s) { long id=Long.parseLong(s); if(id<1) throw new NumberFormatException(); return id; }
}
