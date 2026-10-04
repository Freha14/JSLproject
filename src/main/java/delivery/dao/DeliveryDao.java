package delivery.dao;

import delivery.common.DBManager;
import delivery.dto.DeliveryDto;
import delivery.dto.DeliveryProductDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDao {
    public long insert(DeliveryDto dto) throws SQLException {
        String sql = "INSERT INTO DELIVERY_REQUEST (MEMBER_ID,CENTER_CODE,ORDER_NUMBER,RECIPIENT_NAME,RECIPIENT_PHONE,RECIPIENT_POSTCODE,RECIPIENT_ADDRESS,REQUEST_MEMO,REQUEST_STATUS) VALUES (?,?,?,?,?,?,?,?, 'REQUESTED')";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"REQUEST_ID"})) {
            bindRequest(ps, dto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        throw new SQLException("신청번호를 생성하지 못했습니다.");
    }

    public List<DeliveryDto> findByMember(String memberId) throws SQLException {
        String sql = "SELECT REQUEST_ID,MEMBER_ID,CENTER_CODE,ORDER_NUMBER,RECIPIENT_NAME,RECIPIENT_PHONE,RECIPIENT_POSTCODE,RECIPIENT_ADDRESS,REQUEST_MEMO,REQUEST_STATUS,CREATED_AT,UPDATED_AT FROM DELIVERY_REQUEST WHERE MEMBER_ID=? ORDER BY CREATED_AT DESC";
        List<DeliveryDto> list = new ArrayList<>();
        try (Connection con = DBManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRequest(rs));
            }
        }
        return list;
    }

    public DeliveryDto findDetail(long requestId, String memberId) throws SQLException {
        String sql = "SELECT REQUEST_ID,MEMBER_ID,CENTER_CODE,ORDER_NUMBER,RECIPIENT_NAME,RECIPIENT_PHONE,RECIPIENT_POSTCODE,RECIPIENT_ADDRESS,REQUEST_MEMO,REQUEST_STATUS,CREATED_AT,UPDATED_AT FROM DELIVERY_REQUEST WHERE REQUEST_ID=? AND MEMBER_ID=?";
        DeliveryDto dto = null;
        try (Connection con = DBManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, requestId); ps.setString(2, memberId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) dto = mapRequest(rs); }
        }
        if (dto != null) dto.setProducts(new DeliveryProductDao().findByRequest(requestId));
        return dto;
    }

    public boolean update(DeliveryDto dto, String memberId) throws SQLException {
        String sql = "UPDATE DELIVERY_REQUEST SET CENTER_CODE=?,ORDER_NUMBER=?,RECIPIENT_NAME=?,RECIPIENT_PHONE=?,RECIPIENT_POSTCODE=?,RECIPIENT_ADDRESS=?,REQUEST_MEMO=?,UPDATED_AT=SYSTIMESTAMP WHERE REQUEST_ID=? AND MEMBER_ID=? AND REQUEST_STATUS='REQUESTED'";
        try (Connection con = DBManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,dto.getCenterCode()); ps.setString(2,dto.getOrderNumber());
            ps.setString(3,dto.getRecipientName()); ps.setString(4,dto.getRecipientPhone());
            ps.setString(5,dto.getRecipientPostcode()); ps.setString(6,dto.getRecipientAddress());
            ps.setString(7,dto.getRequestMemo()); ps.setLong(8,dto.getRequestId()); ps.setString(9,memberId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean cancel(long requestId, String memberId) throws SQLException {
        String sql = "UPDATE DELIVERY_REQUEST SET REQUEST_STATUS='CANCELLED',UPDATED_AT=SYSTIMESTAMP WHERE REQUEST_ID=? AND MEMBER_ID=? AND REQUEST_STATUS='REQUESTED'";
        try (Connection con = DBManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1,requestId); ps.setString(2,memberId); return ps.executeUpdate() == 1;
        }
    }

    private void bindRequest(PreparedStatement ps, DeliveryDto d) throws SQLException {
        ps.setString(1,d.getMemberId()); ps.setString(2,d.getCenterCode()); ps.setString(3,d.getOrderNumber());
        ps.setString(4,d.getRecipientName()); ps.setString(5,d.getRecipientPhone()); ps.setString(6,d.getRecipientPostcode());
        ps.setString(7,d.getRecipientAddress()); ps.setString(8,d.getRequestMemo());
    }

    private DeliveryDto mapRequest(ResultSet rs) throws SQLException {
        DeliveryDto d = new DeliveryDto();
        d.setRequestId(rs.getLong("REQUEST_ID")); d.setMemberId(rs.getString("MEMBER_ID"));
        d.setCenterCode(rs.getString("CENTER_CODE")); d.setOrderNumber(rs.getString("ORDER_NUMBER"));
        d.setRecipientName(rs.getString("RECIPIENT_NAME")); d.setRecipientPhone(rs.getString("RECIPIENT_PHONE"));
        d.setRecipientPostcode(rs.getString("RECIPIENT_POSTCODE")); d.setRecipientAddress(rs.getString("RECIPIENT_ADDRESS"));
        d.setRequestMemo(rs.getString("REQUEST_MEMO")); d.setRequestStatus(rs.getString("REQUEST_STATUS"));
        d.setCreatedAt(rs.getTimestamp("CREATED_AT")); d.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
        return d;
    }
}
