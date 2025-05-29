import {createContext, useState, useContext, useEffect} from 'react';
import {login, register} from '../services/AuthService';

const AuthContext = createContext(undefined);

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    const loginUser = async (email, password) => {
        try {
            const response = await login(email, password);
            const userData =
                {
                    id: response?.user?.id ?? "",
                    accessToken: response.accessToken ?? "",
                    email: email,
                    role: response?.user?.role ?? "KORISNIK"
                };

            setUser(userData);
            localStorage.setItem('user', JSON.stringify(userData));
        } catch (error) {
            throw error;
        }
    };

    const logoutUser = () => {
        setUser(null);
        localStorage.removeItem('user');
    };

    const registerUser = async (userData) => {
        try {
            await register(userData);

            const response = await login(userData.email, userData.password);

            const user = {
                id: response?.user?.id ?? "",
                accessToken: response.accessToken ?? "",
                email: userData.email,
                role: response?.user?.role ?? "KORISNIK"
            };
            setUser(user);
            localStorage.setItem('user', JSON.stringify(user));
        } catch (error) {
            throw error;
        }
    };

    return (
        <AuthContext.Provider
            value={{user, login: loginUser, logout: logoutUser, register: registerUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};