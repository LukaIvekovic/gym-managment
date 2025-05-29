import { useState, useEffect } from 'react';
import {
    getAllGroupClasses,
    addParticipant,
    removeParticipant,
    createGroupClass
} from '../../services/GroupClassesService';
import { useNotification } from '../../contexts/NotificationContext';
import Navbar from '../NavBar';
import {useAuth} from "../../contexts/AuthContext.jsx";
import GroupClassCard from "./GroupClassCard.jsx";

const GroupClasses = () => {
    const [groupClasses, setGroupClasses] = useState([]);
    const [loading, setLoading] = useState(true);
    const { showNotification } = useNotification();
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [newClass, setNewClass] = useState({
        name: '',
        description: '',
        type: 'YOGA',
        dateTime: '',
        duration: 60,
        maxParticipants: 10
    });
    const { user } = useAuth();

    const isAuthorized = user?.role === 'ADMIN' || user?.role === 'TRENER';

    useEffect(() => {
        loadGroupClasses();
    }, []);

    const handleCreateSubmit = async (e) => {
        e.preventDefault();
        try {
            await createGroupClass(newClass);
            showNotification('Grupni trening uspješno kreiran', 'success');
            setShowCreateForm(false);
            setNewClass({
                name: '',
                description: '',
                type: 'YOGA',
                dateTime: '',
                duration: 60,
                maxParticipants: 10
            });
            await loadGroupClasses();
        } catch (error) {
            showNotification('Greška pri kreiranju treninga: ' + error.message, 'error');
        }
    };

    const loadGroupClasses = async () => {
        try {
            const data = await getAllGroupClasses();
            setGroupClasses(data);
            setLoading(false);
        } catch (error) {
            showNotification('Greška pri dohvaćanju grupnih treninga: ' + error.message, 'error');
            setLoading(false);
        }
    };

    const handleRegistration = async (classId, isRegistered) => {
        try {
            if (isRegistered) {
                await removeParticipant(classId);
                showNotification('Uspješno ste se odjavili s treninga', 'success');
            } else {
                await addParticipant(classId);
                showNotification('Uspješno ste se prijavili na trening', 'success');
            }
            await loadGroupClasses();
        } catch (error) {
            showNotification('Greška: ' + error.message, 'error');
        }
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-100">
                <Navbar />
                <div className="flex justify-center items-center h-screen">
                    <div>Učitavanje...</div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-100">
            <Navbar />
            <div className="py-10">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-6">Grupni treninzi</h1>
                    {isAuthorized && (
                        <button
                            onClick={() => setShowCreateForm(!showCreateForm)}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 my-2 rounded-md"
                        >
                            {showCreateForm ? 'Odustani' : 'Dodaj novi trening'}
                        </button>
                    )}

                    {showCreateForm && isAuthorized && (
                        <div className="flex justify-center">
                            <div className="mb-8 bg-white p-6 rounded-lg shadow w-1/2">
                                <h2 className="text-xl font-semibold mb-4">Novi grupni trening</h2>
                                <form onSubmit={handleCreateSubmit} className="space-y-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Naziv</label>
                                        <div className="flex justify-center">
                                            <input
                                                type="text"
                                                required
                                                value={newClass.name}
                                                onChange={(e) => setNewClass({...newClass, name: e.target.value})}
                                                className="mt-1 block w-1/2 px-2 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                            />
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Opis</label>
                                        <div className="flex justify-center">
                                            <textarea
                                                required
                                                value={newClass.description}
                                                onChange={(e) => setNewClass({...newClass, description: e.target.value})}
                                                className="mt-1 block w-1/2 px-2 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                            />
                                        </div>
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700">Tip</label>
                                            <div className="flex justify-center">
                                                <select
                                                    value={newClass.type}
                                                    onChange={(e) => setNewClass({...newClass, type: e.target.value})}
                                                    className="mt-1 block w-2/3 px-2 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                                >
                                                    <option value="YOGA">Yoga</option>
                                                    <option value="BOXING">Boxing</option>
                                                    <option value="ZUMBA">Zumba</option>
                                                    <option value="PILATES">Pilates</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700">Datum i vrijeme</label>
                                            <div className="flex justify-center">
                                                <input
                                                    type="datetime-local"
                                                    required
                                                    value={newClass.dateTime}
                                                    onChange={(e) => setNewClass({...newClass, dateTime: e.target.value})}
                                                    className="mt-1 block w-2/3 px-2 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                                />
                                            </div>
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700">Trajanje (min)</label>
                                            <div className="flex justify-center">
                                                <input
                                                    type="number"
                                                    required
                                                    min="15"
                                                    max="180"
                                                    value={newClass.duration}
                                                    onChange={(e) => setNewClass({...newClass, duration: parseInt(e.target.value)})}
                                                    className="mt-1 block w-2/3 px-2 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                                />
                                            </div>
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700">Max polaznika</label>
                                            <div className="flex justify-center">
                                                <input
                                                    type="number"
                                                    required
                                                    min="1"
                                                    max="50"
                                                    value={newClass.maxParticipants}
                                                    onChange={(e) => setNewClass({...newClass, maxParticipants: parseInt(e.target.value)})}
                                                    className="mt-1 block w-2/3 px-2 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="flex justify-end">
                                        <button
                                            type="submit"
                                            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md"
                                        >
                                            Spremi
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}

                    <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
                        {groupClasses.map((groupClass) => (
                            <GroupClassCard key={groupClass.id} groupClass={groupClass} handleRegistration={handleRegistration} />
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default GroupClasses;