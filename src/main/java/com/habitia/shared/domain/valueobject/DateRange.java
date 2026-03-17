package com.habitia.shared.domain.valueobject;

import java.time.LocalDate;

public record DataRange(LocalDate checkIn, LocalDate checkOut) {
    public DataRange{
       if(checkIn == null || checkOut == null)
           throw new IllegalArgumentException("Dates cannot be null");
       if(!checkIn.isBefore(checkOut))
           throw new IllegalArgumentException("checkIn must be before checkOut");
       if(!checkIn.isAfter(LocalDate.now()))
           throw new IllegalArgumentException("chekIn cannot be in the past");
   }
   public long nights(){
        return checkIn.until(checkOut).getDays();
   }
   public boolean overlapsWith(DataRange other){
        return checkIn.isBefore(other.checkOut()) && checkOut.isAfter(other.checkIn());
   }

}

