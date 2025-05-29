import './App.css'
import Registration from "./components/authentication/Registration.jsx";
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import {NotificationProvider} from "./contexts/NotificationContext.jsx";
import {AuthProvider} from "./contexts/AuthContext.jsx";
import Login from "./components/authentication/Login.jsx";
import PrivateRoute from "./components/authentication/PrivateRoute.jsx";
import Home from "./components/Home.jsx";
import UserMembership from "./components/membership/UserMembership.jsx";
import GroupClasses from "./components/groupclass/GroupClasses.jsx";
import Chat from "./components/chat/Chat.jsx";

function App() {

  return (
      <AuthProvider>
          <NotificationProvider>
              <Router>
                  <div className="App">
                      <Routes>
                          <Route path="/login" element={<Login />} />
                          <Route path="/register" element={<Registration />} />
                          <Route path="/home" element={<PrivateRoute><Home /></PrivateRoute>} />
                          <Route path="/membership" element={<PrivateRoute><UserMembership /></PrivateRoute>} />
                          <Route path="/group-classes" element={<PrivateRoute><GroupClasses /></PrivateRoute>} />
                          <Route path="/chat" element={<PrivateRoute><Chat /></PrivateRoute>} />
                          <Route path="/" element={<Login />} />
                      </Routes>
                  </div>
              </Router>
          </NotificationProvider>
      </AuthProvider>
  )
}

export default App
