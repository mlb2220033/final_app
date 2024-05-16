package com.example.finalproject.model;
import java.util.Comparator;

public class BookingHistoryComparator implements Comparator<BookingHistory> {
    @Override
    public int compare(BookingHistory booking1, BookingHistory booking2) {
        // So sánh thời gian của hai mục và trả về kết quả ngược lại
        return booking2.getTime_stamp().compareTo(booking1.getTime_stamp());
    }
}
