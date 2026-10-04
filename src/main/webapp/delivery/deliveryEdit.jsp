<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="delivery.dto.*,java.util.*" %>
<% DeliveryDto d=(DeliveryDto)request.getAttribute("delivery"); %>
<!DOCTYPE html><html lang="ko"><head><meta charset="UTF-8"><title>신청 수정</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/delivery.css"></head><body>
<main class="wrap"><h1>배송신청 수정</h1>
<form method="post" action="<%=request.getContextPath()%>/delivery/update">
<input type="hidden" name="requestId" value="<%=d.getRequestId()%>">
<section class="panel"><label>배송센터 * <select name="centerCode" required><option value="JP-TOKYO" <%= "JP-TOKYO".equals(d.getCenterCode())?"selected":""%>>일본 도쿄</option><option value="JP-OSAKA" <%= "JP-OSAKA".equals(d.getCenterCode())?"selected":""%>>일본 오사카</option><option value="JP-FUKUOKA" <%= "JP-FUKUOKA".equals(d.getCenterCode())?"selected":""%>>일본 후쿠오카</option></select></label>
<label>주문번호 <input name="orderNumber" value="<%=esc(d.getOrderNumber())%>"></label><label>수령인 * <input name="recipientName" required value="<%=esc(d.getRecipientName())%>"></label><label>연락처 * <input name="recipientPhone" required value="<%=esc(d.getRecipientPhone())%>"></label><label>우편번호 <input name="recipientPostcode" value="<%=esc(d.getRecipientPostcode())%>"></label><label>주소 * <input name="recipientAddress" required value="<%=esc(d.getRecipientAddress())%>"></label><label>요청사항 <textarea name="requestMemo"><%=esc(d.getRequestMemo())%></textarea></label></section>
<section class="panel"><h2>상품</h2><p class="hint">기존 상품을 수정하거나 삭제한 뒤, 필요한 상품을 추가할 수 있습니다.</p><div id="products">
<% for(DeliveryProductDto p:d.getProducts()){ %><div class="product"><label>상품명 * <input name="productName" required value="<%=esc(p.getProductName())%>"></label><label>URL <input name="productUrl" type="url" value="<%=esc(p.getProductUrl())%>"></label><label>수량 * <input name="quantity" type="number" min="1" required value="<%=p.getQuantity()%>"></label><label>단가 * <input name="unitPrice" type="number" min="0" step="0.01" required value="<%=p.getUnitPrice()%>"></label><label>분류 <input name="productCategory" value="<%=esc(p.getProductCategory())%>"></label><button type="button" class="remove">상품 삭제</button></div><% } %>
</div><button type="button" id="addProduct">상품 추가</button></section>
<div class="actions"><a href="<%=request.getContextPath()%>/delivery/detail?id=<%=d.getRequestId()%>">돌아가기</a><button type="submit">수정 저장</button></div></form></main>
<script>
const c=document.getElementById('products');document.getElementById('addProduct').onclick=()=>{let d=document.createElement('div');d.className='product';d.innerHTML='<label>상품명 * <input name="productName" required></label><label>URL <input name="productUrl" type="url"></label><label>수량 * <input name="quantity" type="number" min="1" value="1" required></label><label>단가 * <input name="unitPrice" type="number" min="0" step="0.01" value="0" required></label><label>분류 <input name="productCategory"></label><button type="button" class="remove">상품 삭제</button>';c.appendChild(d)};
c.addEventListener('click',e=>{if(e.target.classList.contains('remove')){if(c.querySelectorAll('.product').length>1)e.target.closest('.product').remove();else alert('상품은 최소 1개 필요합니다.')}});
</script></body></html>
<%!
private String esc(String s){if(s==null)return "";return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#x27;");}
%>
