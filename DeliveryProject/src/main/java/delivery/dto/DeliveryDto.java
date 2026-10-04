package delivery.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDto {
    private long requestId;
    private String memberId;
    private String centerCode;
    private String orderNumber;
    private String recipientName;
    private String recipientPhone;
    private String recipientPostcode;
    private String recipientAddress;
    private String requestMemo;
    private String requestStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<DeliveryProductDto> products = new ArrayList<>();

    public long getRequestId() { return requestId; }
    public void setRequestId(long requestId) { this.requestId = requestId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getCenterCode() { return centerCode; }
    public void setCenterCode(String centerCode) { this.centerCode = centerCode; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public String getRecipientPostcode() { return recipientPostcode; }
    public void setRecipientPostcode(String recipientPostcode) { this.recipientPostcode = recipientPostcode; }
    public String getRecipientAddress() { return recipientAddress; }
    public void setRecipientAddress(String recipientAddress) { this.recipientAddress = recipientAddress; }
    public String getRequestMemo() { return requestMemo; }
    public void setRequestMemo(String requestMemo) { this.requestMemo = requestMemo; }
    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public List<DeliveryProductDto> getProducts() { return products; }
    public void setProducts(List<DeliveryProductDto> products) { this.products = products; }
}
