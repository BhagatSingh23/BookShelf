# 📚 BookShelf

A full-stack web application to manage your personal book collection and annotate key highlights from your reads.

🔗 **Live Demo:** [bookshelf01.vercel.app](https://bookshelf01.vercel.app/)

---

## ✨ Features

- Add, view, update, and delete books from your personal collection
- Annotate and store key highlights and notes for each book
- Clean, responsive UI for a smooth reading-management experience
- Persistent data storage — your records are saved across sessions

---

## 🛠️ Tech Stack

**Frontend**
- React.js
- JavaScript
- CSS

**Backend**
- Java
- Spring Boot
- REST API

**Deployment**
- Vercel (Frontend)

---

## 🏗️ Architecture

BookShelf follows a clean separation between frontend and backend:

- The **Spring Boot** backend exposes RESTful APIs for all CRUD operations on books and highlights
- The **React.js** frontend consumes these APIs and renders a responsive, user-friendly interface
- Data is stored persistently through the backend layer

---

## 🚀 Getting Started

### Prerequisites
- Node.js (v16+)
- Java 17+
- Maven

### Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm start
```

The app will open on `http://localhost:3000`

> Make sure to update the API base URL in the frontend to point to your running backend.

---

## 📁 Project Structure

```
BookShelf/
├── frontend/          # React.js application
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── App.js
│   └── package.json
├── backend/           # Spring Boot application
│   ├── src/main/java/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── model/
│   │   └── repository/
│   └── pom.xml
└── README.md
```

---

## 📡 API Endpoints

| Method | Endpoint              | Description              |
|--------|-----------------------|--------------------------|
| GET    | `/api/books`          | Get all books            |
| POST   | `/api/books`          | Add a new book           |
| PUT    | `/api/books/{id}`     | Update a book            |
| DELETE | `/api/books/{id}`     | Delete a book            |
| GET    | `/api/highlights`     | Get all highlights       |
| POST   | `/api/highlights`     | Add a highlight          |
| DELETE | `/api/highlights/{id}`| Delete a highlight       |

---

## 🙋‍♂️ Author

**Bhagat Singh**
[GitHub](https://github.com/BhagatSingh23)
