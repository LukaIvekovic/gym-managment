import { useState, useEffect } from 'react';
import {createMembership, getActiveUserMembership} from '../../services/MembershipService';
import { getAllMembershipTypes } from '../../services/MembershipTypeService';
import { useNotification } from '../../contexts/NotificationContext';
import Navbar from "../NavBar.jsx";

const UserMembership = () => {
    const [activeMembership, setActiveMembership] = useState(null);
    const [membershipTypes, setMembershipTypes] = useState([]);
    const [loading, setLoading] = useState(true);
    const { showNotification } = useNotification();

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [membership, types] = await Promise.all([
                getActiveUserMembership(),
                getAllMembershipTypes()
            ]);

            const active = membership !== null ? membership : null;
            setActiveMembership(active);
            setMembershipTypes(types);
        } catch (error) {
            showNotification('Greška pri dohvaćanju članarina: ' + error.message, 'error');
        } finally {
            setLoading(false);
        }
    };

    const handlePurchase = async (typeId) => {
        try {
            const membershipData = {
                typeId,
                startDate: new Date().toISOString()
            };

            await createMembership(membershipData);
            showNotification('Uspješno ste kupili članarinu!', 'success');
            loadData();
        } catch (error) {
            showNotification('Greška pri kupnji članarine: ' + error.message, 'error');
        }
    };

    const calculateDaysRemaining = (startDate, duration) => {
        const start = new Date(startDate);
        const end = new Date(start.getTime() + duration * 24 * 60 * 60 * 1000);
        const today = new Date();
        const remaining = Math.ceil((end - today) / (1000 * 60 * 60 * 24));
        return remaining > 0 ? remaining : 0;
    };

    if (loading) {
        return <div className="flex justify-center items-center p-8">Učitavanje...</div>;
    }

    return (
        <div className="min-h-screen bg-gray-100">
            <Navbar />
            <div className="max-w-4xl mx-auto p-4">
                <h2 className="text-2xl font-bold mb-6">Moja članarina</h2>

                {activeMembership ? (
                    <div className="bg-white rounded-lg shadow p-6 mb-8">
                        <h3 className="text-xl font-semibold mb-4">Aktivna članarina</h3>
                        <div className="space-y-2">
                            {membershipTypes.find(type => type.id === activeMembership.typeId) && (
                                <>
                                    <p>Tip: {membershipTypes.find(type => type.id === activeMembership.typeId).name}</p>
                                    <p>Početak: {new Date(activeMembership.startDate).toLocaleDateString()}</p>
                                    <p>Preostalo dana: {calculateDaysRemaining(
                                        activeMembership.startDate,
                                        membershipTypes.find(type => type.id === activeMembership.typeId).duration
                                    )}</p>
                                </>
                            )}
                        </div>
                    </div>
                ) : (
                    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-8">
                        <p className="text-yellow-700">Trenutno nemate aktivnu članarinu.</p>
                    </div>
                )}

                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-xl font-semibold mb-4">Dostupne članarine</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {membershipTypes.map(type => (
                            <div key={type.id} className="border rounded-lg p-4">
                                <h4 className="font-semibold">{type.name}</h4>
                                <p className="text-gray-600">{type.description}</p>
                                <p className="text-gray-800 mt-2">Cijena: {type.price} kn</p>
                                <p className="text-gray-800">Trajanje: {type.duration} dana</p>
                                <button
                                    onClick={() => handlePurchase(type.id)}
                                    className="mt-4 w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition-colors"
                                >
                                    Kupi članarinu
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default UserMembership;