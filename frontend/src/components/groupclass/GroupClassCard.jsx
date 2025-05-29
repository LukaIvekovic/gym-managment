import {useGroupClassParticipants} from "../../hooks/UseGroupClassParticipants.js";

const GroupClassCard = ({ groupClass, handleRegistration }) => {
    const liveParticipantCount = useGroupClassParticipants(groupClass);

    return (
        <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-6">
                <h3 className="text-lg font-medium text-gray-900">{groupClass.name}</h3>
                <p className="mt-1 text-gray-500">{groupClass.description}</p>
                <div className="mt-4">
                    <p className="text-sm text-gray-500">
                        Tip: {groupClass.type}
                    </p>
                    <p className="text-sm text-gray-500">
                        Datum: {new Date(groupClass.dateTime).toLocaleString()}
                    </p>
                    <p className="text-sm text-gray-500">
                        Trajanje: {groupClass.duration} minuta
                    </p>
                    <p className="text-sm text-gray-500">
                        Polaznika: {liveParticipantCount}/{groupClass.maxParticipants}
                    </p>
                </div>
                <div className="mt-6">
                    <button
                        onClick={() => handleRegistration(groupClass.id, false)}
                        disabled={liveParticipantCount >= groupClass.maxParticipants}
                        className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white
                                ${liveParticipantCount >= groupClass.maxParticipants
                            ? 'bg-gray-400 cursor-not-allowed'
                            : 'bg-blue-600 hover:bg-blue-700'}`}
                    >
                        Prijavi se
                    </button>
                </div>
            </div>
        </div>
    );
};

export default GroupClassCard;