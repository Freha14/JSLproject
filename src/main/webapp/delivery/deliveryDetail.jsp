<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="delivery.dto.*,java.util.*" %>
<% DeliveryDto d=(DeliveryDto)request.getAttribute("delivery"); %>
<!DOCTYPE html><html lang="ko"><head><meta charset="UTF-8"><title>신청 상세</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/delivery.css"></head><body>
<main class="wrap"><h1>배송신청 상세</h1><section class="panel">
<p><b>신청번호</b> #<%=d.getRequestId()%></p><p><b>상태</b> <%=esc(d.getRequestStatus())%></p>
<p><b>배송센터</b> <%=esc(d.getCenterCode())%></p><p><b>주문번호</b> <%=esc(d.getOrderNumber())%></p>
<p><b>수령인</b> <%=esc(d.getRecipientName())%></p><p><b>연락처</b> <%=esc(d.getRecipientPhone())%></p>
<p><b>주소</b> <%=esc(d.getRecipientPostcode())%> <%=esc(d.getRecipientAddress())%></p><p><b>요청사항</b> <%=esc(d.getRequestMemo())%></p>
</section><section class="panel"><h2>상품 목록</h2><div class="table-wrap"><table><thead><tr><th>상품명</th><th>수량</th><th>단가</th><th>합계</th></tr></thead><tbody>
<% for(DeliveryProductDto p:d.getProducts()){ %><tr><td><%=esc(p.getProductName())%></td><td><%=p.getQuantity()%></td><td><%=p.getUnitPrice()%></td><td><%=p.getLineTotal()%></td></tr><% } %>
</tbody></table></div></section><div class="actions"><a href="<%=request.getContextPath()%>/delivery/list">목록</a>
<% if("REQUESTED".equals(d.getRequestStatus())){ %><a class="button secondary" href="<%=request.getContextPath()%>/delivery/edit?id=<%=d.getRequestId()%>">수정</a>
<form method="post" action="<%=request.getContextPath()%>/delivery/cancel" onsubmit="return confirm('신청을 취소할까요?')"><input type="hidden" name="requestId" value="<%=d.getRequestId()%>"><button class="danger">신청 취소</button></form><% } %></div></main></body></html>
<%!
private String esc(String s){if(s==null)return "";return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#x27;");}
%>
