-- Create appointment status enum type
CREATE TYPE appointment_status AS ENUM ('SCHEDULED', 'CANCELLED', 'COMPLETED', 'NO_SHOW');

-- Drop the default constraint first
ALTER TABLE appointments ALTER COLUMN status DROP DEFAULT;

-- Update existing appointments table to use the new enum
ALTER TABLE appointments 
    ALTER COLUMN status TYPE appointment_status 
    USING 
        CASE status 
            WHEN 'BOOKED' THEN 'SCHEDULED'::appointment_status
            WHEN 'CANCELLED' THEN 'CANCELLED'::appointment_status
            ELSE 'SCHEDULED'::appointment_status
        END;

-- Set the new default value
ALTER TABLE appointments ALTER COLUMN status SET DEFAULT 'SCHEDULED'::appointment_status; 