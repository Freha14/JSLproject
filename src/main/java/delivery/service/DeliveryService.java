package delivery.service;

import delivery.common.DBManager;
import delivery.dao.DeliveryDao;
import delivery.dao.DeliveryProductDao;
import delivery.dto.DeliveryDto;
import java.sql.Connection;
import java.sql.SQLException;

public class DeliveryService {
    private final DeliveryDao deliveryDao = new DeliveryDao();
    private final DeliveryProductDao productDao = new DeliveryProductDao();

    public long create(DeliveryDto dto) throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                long id;
                String sql = "INSERT INTO DELIVERY_REQUEST (MEMBER_ID,CENTER_CODE,ORDER_NUMBER,RECIPIENT_NAME,RECIPIENT_PHONE,RECIPIENT_POSTCODE,RECIPIENT_ADDRESS,REQUEST_MEMO,REQUEST_STATUS) VALUES (?,?,?,?,?,?,?,?, 'REQUESTED')";
                try (java.sql.PreparedStatement ps = con.prepareStatement(sql, new String[]{"REQUEST_ID"})) {
                    ps.setString(1,dto.getMemberId()); ps.setString(2,dto.getCenterCode()); ps.setString(3,dto.getOrderNumber());
                    ps.setString(4,dto.getRecipientName()); ps.setString(5,dto.getRecipientPhone()); ps.setString(6,dto.getRecipientPostcode());
                    ps.setString(7,dto.getRecipientAddress()); ps.setString(8,dto.getRequestMemo()); ps.executeUpdate();
                    try (java.sql.ResultSet rs=ps.getGeneratedKeys()) { if(!rs.next()) throw new SQLException("신청번호 생성 실패"); id=rs.getLong(1); }
                }
                productDao.insert(con,id,dto.getProducts());
                con.commit();
                return id;
            } catch (SQLException | RuntimeException e) {
                con.rollback(); throw e;
            }
        }
    }

    public boolean update(DeliveryDto dto, String memberId) throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                String sql = "UPDATE DELIVERY_REQUEST SET CENTER_CODE=?,ORDER_NUMBER=?,RECIPIENT_NAME=?,RECIPIENT_PHONE=?,RECIPIENT_POSTCODE=?,RECIPIENT_ADDRESS=?,REQUEST_MEMO=?,UPDATED_AT=SYSTIMESTAMP WHERE REQUEST_ID=? AND MEMBER_ID=? AND REQUEST_STATUS='REQUESTED'";
                int count;
                try (java.sql.PreparedStatement ps=con.prepareStatement(sql)) {
                    ps.setString(1,dto.getCenterCode()); ps.setString(2,dto.getOrderNumber()); ps.setString(3,dto.getRecipientName());
                    ps.setString(4,dto.getRecipientPhone()); ps.setString(5,dto.getRecipientPostcode()); ps.setString(6,dto.getRecipientAddress());
                    ps.setString(7,dto.getRequestMemo()); ps.setLong(8,dto.getRequestId()); ps.setString(9,memberId);
                    count=ps.executeUpdate();
                }
                if(count!=1) { con.rollback(); return false; }
                productDao.replaceForRequest(con,dto.getRequestId(),dto.getProducts());
                con.commit(); return true;
            } catch(SQLException | RuntimeException e) { con.rollback(); throw e; }
        }
    }
}
