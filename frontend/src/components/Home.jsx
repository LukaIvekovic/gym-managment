import { useAuth } from '../contexts/AuthContext';
import Navbar from "./NavBar.jsx";

const Home = () => {
    const { user } = useAuth();

    return (
        <div className="min-h-screen bg-gray-100">
            <Navbar />
            <div className="py-12 px-4 sm:px-6 lg:px-8">
                <div className="max-w-3xl mx-auto bg-white rounded-lg shadow-lg p-8">
                    <h1 className="text-3xl font-bold text-gray-900 text-center mb-6">
                        Dobrodošli u sustav
                    </h1>
                    <p className="text-xl text-gray-700 text-center">
                        {user?.email ? `Prijavljeni ste kao: ${user.email}` : 'Dobrodošli gost'}
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Home;