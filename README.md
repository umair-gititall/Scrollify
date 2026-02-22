# Scrollify App

An Android mobile application featuring a secure user authentication system and a fully functional CRUD (Create, Read, Update, Delete) interface for managing a personal library of books.

---

## 🚀 Features & UI Flow

* **Animated UI Elements:**
    * Startup page features an animated background and welcome text.
    * `imagePreview.java` cycles through book images in the Add Book and Startup screens.
    * `textPreview.java` creates typing-style animations for messages on startup and settings screens.
* **User Authentication:**
    * **Sign Up:** Users register by providing a username, email, password, and date of birth.
    * **Login:** Users authenticate using their email and password.
    * **Forgot Password:** Users can request a password reset via email.
* **Home Screen (Library):** Displays the user’s personal book collection in a horizontal `RecyclerView` equipped with page snapping.
* **Book Management (CRUD):**
    * Users can input book details including Title, Author, ISBN, Year, and Image.
    * Users can add new books or edit existing ones. When editing, the ISBN field is disabled.
* **Account Settings:**
    * Users can log out to end their session.
    * Users can delete their account, which requires password confirmation and securely wipes all user data and the account itself.

---

## 🛠 Technical Stack & Architecture

* **Platform:** Android
* **Backend:** Firebase Authentication, Firebase Realtime Database, Cloud Firestore
* **Architecture:** Database logic is separated from the UI and entirely managed within `DatabaseHandler.java`.
* **Local Caching:** `SharedPreferences` is used to cache book data for faster loading and to store session/UI state flags (like logged-in status).

---

## 📄 Database Design & Flow

### 1. Authentication Flow
Authentication is explicitly managed via `FirebaseAuth` inside `DatabaseHandler`, utilizing callbacks to inform the UI of success or failure.

* **Registration:** `FirebaseAuth` creates the user, generates a unique UID, and saves the user details in the Realtime Database under that UID.
* **Login:** The system retrieves the email using the username from the Realtime Database, then signs in using `signInWithEmailAndPassword`.
* **Account Deletion:** Requires the user to re-enter their password for re-authentication. Once confirmed, a `WriteBatch` deletes all books from Firestore, the user profile is removed from the Realtime Database, and the `FirebaseAuth` account is deleted.

### 2. Database Structure
The project utilizes a hybrid database approach:

#### **Firebase Realtime Database (User Profiles)**
* **Node:** `EMAILS`
    * **Key:** Sanitized Email String (e.g., `user,domain,com`)
    * **Fields:** `PASSWORD`, `USERNAME`
* **Node:** `USERS`
    * **Key:** User UID
    * **Fields:** `dob`, `email`, `username`

#### **Cloud Firestore (Book Collections)**
* **Collection:** Books are saved under a specific collection or user-specific path.
    * **Document ID:** The book's ISBN (e.g., `78996435`) acts as the unique document ID.
    * **Fields:**
        * `AuthorName` (String)
        * `ISBN` (String)
        * `Image` (Number/Integer)
        * `Title` (String)
        * `Year` (Number/String)

---

### 3. CRUD Operations
* **Add Book:** Saves new book under the user’s UID in Firestore using the ISBN as the document ID.
* **Update Book:** Pre-fills the form with existing data, disables the ISBN field, and overwrites the document in Firestore with updated details.
* **View Books:** Retrieves books from Firestore under the user’s UID and displays them in a `RecyclerView` with page snapping.
* **Delete Book:** Deletes the specific book document using the user’s UID and ISBN, subsequently updating the UI and local `SharedPreferences` cache.

---

## ⚙️ Setup & Installation

To run this project locally, you must connect it to your own Firebase backend:

### 1. Firebase Configuration
1. Create a new project in the [Firebase Console](https://console.firebase.google.com/).
2. Register the Android app using your package name and download the `google-services.json` file.
3. Place `google-services.json` in the `app/` directory of the project.
4. Enable **Email/Password** authentication in the Firebase Console.

### 2. Database Setup
1. **Realtime Database:** Create a database to store user profile information.
2. **Cloud Firestore:** Create a Firestore database to store book collections.
3. **Security Rules:** Ensure your rules allow authenticated users to read and write data:
**Realtime Rules:**
```javascript
{
  "rules": {
    ".read":true,
      ".write":true,
     "EMAILS": {
      ".read": true,
			".write": true,
    },
      "FINALUSERS":{
        ".read": true,
          ".write": true,
      },
    "USERS": {
      "$uid": {
        ".read": "auth != null && auth.uid == $uid",
        ".write": "auth != null && auth.uid == $uid"
      }
    }
  }
}
```
**Firestore Rules:**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
