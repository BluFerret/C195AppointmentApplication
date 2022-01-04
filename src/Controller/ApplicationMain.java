package Controller;

public class ApplicationMain {
    // TODO: Customer records and appointments can be added, updated, and deleted. When deleting a customer record,
    //  all of the customer’s appointments must be deleted first, due to foreign key constraints.
    // TODO: When adding and updating a customer, text fields are used to collect the following data: customer name,
    //  address, postal code, and phone number. Customer IDs are auto-generated, and first-level division
    //  (i.e., states, provinces) and country data are collected using separate combo boxes.
    // TODO: Country and first-level division data is pre-populated in separate combo boxes or lists in the user
    //  interface for the user to choose. The first-level list should be filtered by the user’s selection of a
    //  country (e.g., when choosing U.S., filter so it only shows states).
    // TODO: All of the original customer information is displayed on the update form. All of the fields can be
    //  updated except for Customer_ID. Customer data is displayed using a TableView, including first-level division
    //  data. A list of all the customers and their information may be viewed in a TableView, and updates of the data
    //  can be performed in text fields on the form.
    //  TODO: When a customer record is deleted, a custom message should display in the user interface.
    //  TODO: Write code that enables the user to add, update, and delete appointments. A contact name is assigned to
    //   an appointment using a drop-down menu or combo box. A custom message is displayed in the user interface with
    //   the Appointment_ID and type of appointment canceled. The Appointment_ID is auto-generated and disabled
    //   throughout the application. When adding and updating an appointment, record the following data:
    //   Appointment_ID, title, description, location, contact, type, start date and time, end date and time,
    //   Customer_ID, and User_ID. All of the original appointment information is displayed on the update form in
    //   local time zone. All of the appointment fields can be updated except Appointment_ID, which must be disabled.
    //   TODO: Write code that enables the user to adjust appointment times. While the appointment times should be
    //    stored in Coordinated Universal Time (UTC), they should be automatically and consistently updated according
    //    to the local time zone set on the user’s computer wherever appointments are displayed in the application.
    //    Note: There are up to three time zones in effect. Coordinated Universal Time (UTC) is used for storing the
    //    time in the database, the user’s local time is used for display purposes, and Eastern Standard Time (EST) is
    //    used for the company’s office hours. Local time will be checked against EST business hours before they are
    //    stored in the database as UTC.
    //    TODO: Write code to implement input validation and logical error checks to prevent each of the following
    //     changes when adding or updating information; display a custom message specific for each error check in the
    //     user interface: scheduling an appointment outside of business hours defined as 8:00 a.m. to 10:00 p.m. EST,
    //     including weekends. scheduling overlapping appointments for customers. entering an incorrect username and
    //     password.
    //    TODO: Write code to provide an alert when there is an appointment within 15 minutes of the user’s log-in.
    //     A custom message should be displayed in the user interface and include the appointment ID, date, and time.
    //     If the user does not have any appointments within 15 minutes of logging in, display a custom message in the
    //     user interface indicating there are no upcoming appointments. Note: Since evaluation may be testing your
    //     application outside of business hours, your alerts must be robust enough to trigger an appointment within
    //     15 minutes of the local time set on the user’s computer, which may or may not be EST.
    //    TODO: Write code that generates accurate information in each of the following reports and will display the
    //     reports in the user interface: Note: You do not need to save and print the reports to a file or provide
    //     a screenshot.the total number of customer appointments by type and month. a schedule for each contact in
    //     your organization that includes appointment ID, title, type and description, start date and time, end date
    //     and time, and customer ID. an additional report of your choice that is different from the two other required
    //     reports in this prompt and from the user log-in date and time stamp that will be tracked in part C.
    //    TODO: Write at least two different lambda expressions to improve your code.
    //    TODO: Write code that provides the ability to track user activity by recording all user log-in attempts,
    //     dates, and time stamps and whether each attempt was successful in a file named login_activity.txt. Append
    //     each new record to the existing file, and save to the root folder of the application.
}
