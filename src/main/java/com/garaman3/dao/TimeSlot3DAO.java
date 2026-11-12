package com.garaman3.dao;

import com.garaman3.entity.TimeSlot3;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lop truy xuat du lieu cho TimeSlot3.
 * Phuc vu Module 1: Book Appointment Online 3.
 */
public class TimeSlot3DAO {

    private Connection conn;

    public TimeSlot3DAO(Connection conn) {
        this.conn = conn;
    }

    public boolean lockSlot(String slotId) throws SQLException {
        String sql = "SELECT id FROM tblTimeSlot3 WHERE id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slotId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Lay danh sach khung gio va tinh so cho con lai cua tung khung gio
     * trong mot ngay cu the (remaining la thuoc tinh dan xuat, tinh dong
     * bang capacity tru so Appointment3 chua huy cua khung gio do trong ngay do).
     *
     * @param appointmentDate ngay can xem khung gio
     * @return danh sach TimeSlot3, moi phan tu co capacity va remainingCapacity
     */
    public List<TimeSlot3> getSlotsByDate(LocalDate appointmentDate) throws SQLException {
        String sql =
            "SELECT t.id, t.startTime, t.endTime, t.capacity, " +
            "  (t.capacity - COUNT(a.id)) AS remaining " +
            "FROM tblTimeSlot3 t " +
            "LEFT JOIN tblAppointment3 a " +
            "  ON a.timeSlotId = t.id AND a.appointmentDate = ? AND a.status <> 'Cancelled' " +
            "GROUP BY t.id, t.startTime, t.endTime, t.capacity " +
            "ORDER BY t.startTime";

        List<TimeSlot3> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, appointmentDate.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TimeSlot3 slot = new TimeSlot3();
                    slot.setId(rs.getString("id"));
                    slot.setStartTime(java.time.LocalTime.parse(rs.getString("startTime")));
                    slot.setEndTime(java.time.LocalTime.parse(rs.getString("endTime")));
                    slot.setCapacity(rs.getInt("capacity"));
                    slot.setRemainingCapacity(Math.max(0, rs.getInt("remaining")));
                    result.add(slot);
                }
            }
        }
        return result;
    }
}
