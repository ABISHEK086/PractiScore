package com.example.myapplication;

public class StaffModel {

    public static class Staff {
        private String email; 
        private String role;
  
        // Default constructor (needed for Firebase)
        public Staff() {  
        } 
 
        // Constructor with parameters
        public Staff(String email, String role) {
            this.email = email;
            this.role = role;
        }

        // Getter for email 
        public String getEmail() {
            return email;
        }

        // Setter for email
        public void setEmail(String email) {
            this.email = email;
        }

        // Getter for role
        public String getRole() {
            return role;
        }

        // Setter for role
        public void setRole(String role) {
            this.role = role;
        }
    }
}








