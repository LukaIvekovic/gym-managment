import {useState, useEffect, useRef} from 'react';
import {getTrainers, getGymGoers} from '../../services/AuthService';
import {useAuth} from '../../contexts/AuthContext';
import SockJS from 'sockjs-client';
import {Client} from '@stomp/stompjs';
import {useNotification} from '../../contexts/NotificationContext';
import Navbar from '../NavBar';

const Chat = () => {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const {user} = useAuth();
    const {showNotification} = useNotification();
    const stompClientRef = useRef(null);

    useEffect(() => {
        loadUsers();
        setupWebSocket();
        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
            }
        };
    }, [user]);

    const loadUsers = async () => {
        try {
            const data = user?.role === 'TRENER' ? await getGymGoers() : await getTrainers();
            setUsers(data);
        } catch (error) {
            showNotification('Error loading users: ' + error.message, 'error');
        }
    };

    const setupWebSocket = () => {
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log('Connected to WebSocket');
                stompClient.subscribe(`/user/${user.id}/queue/messages`, (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    setMessages(prevMessages => [...prevMessages, receivedMessage]);
                });
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
        });

        stompClient.activate();
        stompClientRef.current = stompClient;
    };

    const handleUserSelect = (selectedUser) => {
        setSelectedUser(selectedUser);
        setMessages([]);
    };

    const sendMessage = () => {
        if (!newMessage.trim() || !selectedUser || !stompClientRef.current) return;

        const message = {
            senderId: user.id,
            receiverId: selectedUser.id,
            content: newMessage,
            timestamp: new Date().toISOString()
        };

        stompClientRef.current.publish({
            destination: '/app/chat',
            body: JSON.stringify(message)
        });

        setMessages(prev => [...prev, message]);
        setNewMessage('');
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <Navbar />
            <div className="flex h-[calc(100vh-4rem)] mx-auto max-w-7xl px-4 py-6">
                <div className="w-1/4 bg-white rounded-l-lg shadow-sm overflow-y-auto">
                    <h2 className="p-4 text-lg font-semibold border-b">{user?.role === 'TRENER' ? 'Korisnici' : 'Treneri'}</h2>
                    <div className="divide-y">
                        {users.map((u) => (
                            <div
                                key={u.id}
                                onClick={() => handleUserSelect(u)}
                                className={`p-4 cursor-pointer hover:bg-gray-50 
                                ${selectedUser?.id === u.id ? 'bg-blue-50' : ''}`}
                            >
                                <div className="font-medium">{u.firstName} {u.lastName}</div>
                                <div className="text-sm text-gray-500">{u.email}</div>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="w-3/4 bg-white rounded-r-lg shadow-sm flex flex-col">
                    {selectedUser ? (
                        <>
                            <div className="p-4 border-b">
                                <h3 className="font-medium">
                                    Razgovor sa {selectedUser.firstName} {selectedUser.lastName}
                                </h3>
                            </div>

                            <div className="flex-1 p-4 overflow-y-auto">
                                {messages.map((msg, index) => (
                                    <div
                                        key={index}
                                        className={`max-w-[70%] mb-4 p-3 rounded-lg 
                                        ${msg.senderId === user.id
                                            ? 'ml-auto bg-blue-500 text-white'
                                            : 'bg-gray-100'}`}
                                    >
                                        <div>{msg.content}</div>
                                        <div className={`text-xs mt-1 
                                            ${msg.senderId === user.id ? 'text-blue-100' : 'text-gray-500'}`}>
                                            {new Date(msg.timestamp).toLocaleTimeString()}
                                        </div>
                                    </div>
                                ))}
                            </div>

                            <div className="p-4 border-t">
                                <div className="flex gap-2">
                                    <input
                                        type="text"
                                        value={newMessage}
                                        onChange={(e) => setNewMessage(e.target.value)}
                                        onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                                        className="flex-1 p-2 border rounded-md"
                                        placeholder="Napišite poruku..."
                                    />
                                    <button
                                        onClick={sendMessage}
                                        className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                                    >
                                        Pošalji
                                    </button>
                                </div>
                            </div>
                        </>
                    ) : (
                        <div className="flex-1 flex items-center justify-center text-gray-500">
                            Odaberite korisnika za razgovor
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Chat;