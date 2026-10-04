<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="delivery.dto.DeliveryDto,delivery.dto.DeliveryProductDto,java.util.*" %>
<%
DeliveryDto d=(DeliveryDto)request.getAttribute("delivery");
if(d==null) d=new DeliveryDto();
String error=(String)request.getAttribute("error");
%>
<!DOCTYPE html><html lang="ko"><head><meta charset="UTF-8"><title>배송대행 신청</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/delivery.css"></head><body>
<main class="wrap"><h1>배송대행 신청</h1>
<% if(error!=null){ %><p class="error"><%=error.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")%></p><% } %>
<form method="post" action="<%=request.getContextPath()%>/delivery/save" id="deliveryForm">
<section class="panel"><h2>배송 정보</h2>
<label>배송센터 * <select name="centerCode" required>
<option value="">선택</option>
<option value="JP-TOKYO" <%= "JP-TOKYO".equals(d.getCenterCode())?"selected":"" %>>일본 도쿄</option>
<option value="JP-OSAKA" <%= "JP-OSAKA".equals(d.getCenterCode())?"selected":"" %>>일본 오사카</option>
<option value="JP-FUKUOKA" <%= "JP-FUKUOKA".equals(d.getCenterCode())?"selected":"" %>>일본 후쿠오카</option>
</select></label>
<label>쇼핑몰 주문번호 <input name="orderNumber" maxlength="100" value="<%=esc(d.getOrderNumber())%>"></label>
<label>수령인 * <input name="recipientName" required maxlength="100" value="<%=esc(d.getRecipientName())%>"></label>
<label>연락처 * <input name="recipientPhone" required maxlength="30" value="<%=esc(d.getRecipientPhone())%>"></label>
<label>우편번호 <input name="recipientPostcode" maxlength="20" value="<%=esc(d.getRecipientPostcode())%>"></label>
<label>주소 * <input name="recipientAddress" required maxlength="500" value="<%=esc(d.getRecipientAddress())%>"></label>
<label>요청사항 <textarea name="requestMemo" maxlength="1000"><%=esc(d.getRequestMemo())%></textarea></label>
</section>
<section class="panel"><div class="row"><h2>상품 정보</h2><button type="button" id="addProduct">상품 추가</button></div>
<div id="products">
<% List<DeliveryProductDto> ps=d.getProducts(); if(ps==null||ps.isEmpty()) ps=Arrays.asList(new DeliveryProductDto());
for(DeliveryProductDto p:ps){ %>
<div class="product">
<label>상품명 * <input name="productName" required maxlength="200" value="<%=esc(p.getProductName())%>"></label>
<label>상품 URL <input name="productUrl" type="url" maxlength="1000" value="<%=esc(p.getProductUrl())%>"></label>
<label>수량 * <input name="quantity" type="number" min="1" value="<%=p.getQuantity()>0?p.getQuantity():1%>" required></label>
<label>개당 가격 * <input name="unitPrice" type="number" min="0" step="0.01" value="<%=p.getUnitPrice()==null?"0":p.getUnitPrice()%>" required></label>
<label>상품 분류 <input name="productCategory" maxlength="100" value="<%=esc(p.getProductCategory())%>"></label>
<button type="button" class="remove">상품 삭제</button>
</div><% } %>
</div></section>
<div class="actions"><a href="<%=request.getContextPath()%>/delivery/list">목록</a><button type="submit">신청하기</button></div>
</form></main>
<script>
const container=document.getElementById('products');
document.getElementById('addProduct').addEventListener('click',()=>{
 const div=document.createElement('div'); div.className='product';
 div.innerHTML='<label>상품명 * <input name="productName" required maxlength="200"></label>'+
 '<label>상품 URL <input name="productUrl" type="url" maxlength="1000"></label>'+
 '<label>수량 * <input name="quantity" type="number" min="1" value="1" required></label>'+
 '<label>개당 가격 * <input name="unitPrice" type="number" min="0" step="0.01" value="0" required></label>'+
 '<label>상품 분류 <input name="productCategory" maxlength="100"></label>'+
 '<button type="button" class="remove">상품 삭제</button>';
 container.appendChild(div);
});
container.addEventListener('click',e=>{
 if(e.target.classList.contains('remove')){
  if(container.querySelectorAll('.product').length>1) e.target.closest('.product').remove();
  else alert('상품은 최소 1개 필요합니다.');
 }
});
</script></body></html>
<%!
private String esc(String s) {
 if(s==null)return "";
 return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#x27;");
}
%>
