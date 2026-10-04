package delivery.dao;

import delivery.common.DBManager;
import delivery.dto.DeliveryProductDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProductDao {
    public void insert(Connection con, long requestId, List<DeliveryProductDto> products) throws SQLException {
        String sql = "INSERT INTO DELIVERY_PRODUCT (REQUEST_ID,PRODUCT_NAME,PRODUCT_URL,QUANTITY,UNIT_PRICE,PRODUCT_CATEGORY) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (DeliveryProductDto p : products) {
                ps.setLong(1,requestId); ps.setString(2,p.getProductName()); ps.setString(3,p.getProductUrl());
                ps.setInt(4,p.getQuantity()); ps.setBigDecimal(5,p.getUnitPrice()); ps.setString(6,p.getProductCategory());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<DeliveryProductDto> findByRequest(long requestId) throws SQLException {
        List<DeliveryProductDto> list = new ArrayList<>();
        String sql = "SELECT PRODUCT_ID,REQUEST_ID,PRODUCT_NAME,PRODUCT_URL,QUANTITY,UNIT_PRICE,PRODUCT_CATEGORY FROM DELIVERY_PRODUCT WHERE REQUEST_ID=? ORDER BY PRODUCT_ID";
        try (Connection con = DBManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1,requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DeliveryProductDto p = new DeliveryProductDto();
                    p.setProductId(rs.getLong("PRODUCT_ID")); p.setRequestId(rs.getLong("REQUEST_ID"));
                    p.setProductName(rs.getString("PRODUCT_NAME")); p.setProductUrl(rs.getString("PRODUCT_URL"));
                    p.setQuantity(rs.getInt("QUANTITY")); p.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
                    p.setProductCategory(rs.getString("PRODUCT_CATEGORY")); list.add(p);
                }
            }
        }
        return list;
    }

    public void replaceForRequest(Connection con, long requestId, List<DeliveryProductDto> products) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM DELIVERY_PRODUCT WHERE REQUEST_ID=?")) {
            ps.setLong(1,requestId); ps.executeUpdate();
        }
        insert(con,requestId,products);
    }
}
