<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*,delivery.dto.DeliveryDto" %>
<%
List<DeliveryDto> list=(List<DeliveryDto>)request.getAttribute("deliveries");
%>
<!DOCTYPE html><html lang="ko"><head><meta charset="UTF-8"><title>내 배송신청</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/delivery.css"></head><body>
<main class="wrap"><div class="row"><h1>내 배송신청</h1><a class="button" href="<%=request.getContextPath()%>/delivery/new">신청하기</a></div>
<% if(list==null||list.isEmpty()){ %><section class="panel">등록된 배송신청이 없습니다.</section><% } else { %>
<div class="panel table-wrap"><table><thead><tr><th>신청번호</th><th>센터</th><th>수령인</th><th>상태</th><th>신청일</th><th>상세</th></tr></thead><tbody>
<% for(DeliveryDto d:list){ %><tr><td><%=d.getRequestId()%></td><td><%=esc(d.getCenterCode())%></td><td><%=esc(d.getRecipientName())%></td><td><%=status(d.getRequestStatus())%></td><td><%=d.getCreatedAt()%></td><td><a href="<%=request.getContextPath()%>/delivery/detail?id=<%=d.getRequestId()%>">보기</a></td></tr><% } %>
</tbody></table></div><% } %></main></body></html>
<%!
private String esc(String s){if(s==null)return "";return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#x27;");}
private String status(String s){if("REQUESTED".equals(s))return "신청 접수";if("ARRIVED".equals(s))return "입고 완료";if("PREPARING".equals(s))return "출고 준비";if("SHIPPED".equals(s))return "배송 중";if("DELIVERED".equals(s))return "배송 완료";if("CANCELLED".equals(s))return "취소";return s==null?"":s;}
%>
