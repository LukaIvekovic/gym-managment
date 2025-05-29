import {useState, useEffect} from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

export function useGroupClassParticipants(groupClass) {
  const [participantCount, setParticipantCount] = useState(groupClass.currentParticipants);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws', null, {
      transports: ['websocket', 'xhr-streaming', 'xhr-polling'],
      withCredentials: false
    });
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => {
        console.log(str);
      },
      onConnect: () => {
        console.log('Connected to WebSocket');
        stompClient.subscribe('/topic/group-class/' + groupClass.id + '/participants', (response) => {
          const data = JSON.parse(response.body);
          setParticipantCount(data.participantCount);
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [groupClass.id]);

  return participantCount;
}