import java.io.*;
import java.util.*;

// -------- Room Class --------
class Room {
    private int roomNumber;
    private String type;
    private double price;
    private boolean isAvailable;

    public Room(int roomNumber, String type, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") - $" + price;
    }
}

// -------- Reservation Class --------
class Reservation {
    public static int counter = 1;   // Made public to fix the error
    private int id;
    private String customerName;
    private Room room;

    public Reservation(String name, Room room) {
        this.id = counter++;
        this.customerName = name;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return id + "|" + customerName + "|" + room.getRoomNumber() + "|" + room.getType();
    }
}

// -------- Booking System (File I/O + Logic) --------
class BookingSystem {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private File file = new File("bookings.txt");

    public BookingSystem() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        loadRooms();
        loadBookingsFromFile();
    }

    // Preload some rooms
    private void loadRooms() {
        rooms.add(new Room(101, "Standard", 100));
        rooms.add(new Room(102, "Standard", 100));
        rooms.add(new Room(201, "Deluxe", 180));
        rooms.add(new Room(202, "Deluxe", 180));
        rooms.add(new Room(301, "Suite", 250));
        rooms.add(new Room(302, "Suite", 250));
    }

    public void showAvailableRooms() {
        System.out.println("Available Rooms:");
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.println(room);
            }
        }
    }

    public Room getAvailableRoomByType(String type) {
        for (Room room : rooms) {
            if (room.getType().equalsIgnoreCase(type) && room.isAvailable()) {
                return room;
            }
        }
        return null;
    }

    public Reservation bookRoom(String customerName, Room room) {
        if (room != null && room.isAvailable()) {
            room.setAvailable(false);
            Reservation res = new Reservation(customerName, room);
            reservations.add(res);
            saveBookingToFile(res);
            return res;
        }
        return null;
    }

    public boolean cancelBooking(int bookingId) {
        Iterator<Reservation> iter = reservations.iterator();
        while (iter.hasNext()) {
            Reservation r = iter.next();
            if (r.getId() == bookingId) {
                r.getRoom().setAvailable(true);
                iter.remove();
                saveAllBookingsToFile();
                return true;
            }
        }
        return false;
    }

    public void viewAllBookings() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.println("BookingID: " + r.getId() + " - " + r.getCustomerName() +
                    " - Room " + r.getRoom().getRoomNumber() + " (" + r.getRoom().getType() + ")");
        }
    }

    // --- File I/O ---
    private void saveBookingToFile(Reservation reservation) {
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(reservation.toString() + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    private void saveAllBookingsToFile() {
        try {
            FileWriter fw = new FileWriter(file, false); // overwrite
            for (Reservation r : reservations) {
                fw.write(r.toString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error updating file.");
        }
    }

    private void loadBookingsFromFile() {
        if (!file.exists()) return;
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int roomNum = Integer.parseInt(parts[2]);

                    // find the room and mark unavailable
                    for (Room room : rooms) {
                        if (room.getRoomNumber() == roomNum) {
                            room.setAvailable(false);
                            Reservation r = new Reservation(name, room);
                            // ensure counter is updated
                            while (Reservation.counter <= id) {
                                Reservation.counter++;
                            }
                            reservations.add(r);
                            break;
                        }
                    }
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading bookings from file.");
        }
    }
}

// -------- Main App --------
public class HotelBookingApp {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== HOTEL BOOKING MENU ======");
            System.out.println("1. Show Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Reservations");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    system.showAvailableRooms();
                    break;

                case 2:
                    sc.nextLine(); // clear buffer
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room type (Standard/Deluxe/Suite): ");
                    String type = sc.nextLine();
                    Room room = system.getAvailableRoomByType(type);
                    if (room != null) {
                        System.out.println("Room Found: " + room.toString());
                        System.out.print("Proceed with payment? (yes/no): ");
                        String pay = sc.nextLine();
                        if (pay.equalsIgnoreCase("yes")) {
                            Reservation res = system.bookRoom(name, room);
                            if (res != null) {
                                System.out.println("Booking successful! Your Booking ID = " + res.getId());
                            }
                        } else {
                            System.out.println("Payment canceled.");
                        }
                    } else {
                        System.out.println("No available rooms of that type.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Booking ID to cancel: ");
                    int bid = sc.nextInt();
                    if (system.cancelBooking(bid)) {
                        System.out.println("Booking canceled successfully.");
                    } else {
                        System.out.println("Booking ID not found.");
                    }
                    break;

                case 4:
                    system.viewAllBookings();
                    break;

                case 5:
                    System.out.println("Thank you for using the Hotel Booking System!");
                    System.exit(0);

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}