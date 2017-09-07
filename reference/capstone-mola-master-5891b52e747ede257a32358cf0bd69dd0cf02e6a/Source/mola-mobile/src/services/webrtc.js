import {
  RTCPeerConnection,
  RTCMediaStream,
  RTCIceCandidate,
  RTCSessionDescription,
  RTCView,
  MediaStreamTrack,
  getUserMedia,
} from 'react-native-webrtc';
import { Platform, } from 'react-native';
import InCallManager from 'react-native-incall-manager';

export const getLocalStream = (isFront, callback) => {  
  let videoSourceId;
  getUserMedia({
    audio: true,
    video: {
      mandatory: {
        minWidth: 640,
        minHeight: 360,
        minFrameRate: 30,
      },
      facingMode: (isFront ? "user" : "environment"),
      optional: (videoSourceId ? [{sourceId: videoSourceId}] : []),
    }
  }, function (stream) {
    callback(stream);
  }, logError);
}

export const join = (room, socket, createPeerConnection) => {
  socket.emit('join', room, socketIds => {
    console.log('join', socketIds);
    for (const i in socketIds) {
      const socketId = socketIds[i];
      createPeerConnection(socketId, true);
    }
  });
}

export const exchange = (data, pcPeers, createPeerConnection, socket) => {
  const fromId = data.from;
  let peer;
  if (fromId in pcPeers) {
    peer = pcPeers[fromId];
  } else {
    peer = createPeerConnection(fromId, false);
  }

  if (data.sdp) {
    // console.log('exchange sdp', data);
    peer.setRemoteDescription(new RTCSessionDescription(data.sdp), () => {
      if (peer.remoteDescription.type == "offer")
        peer.createAnswer(desc => {
          // console.log('createAnswer', desc);
          peer.setLocalDescription(desc, () => {
            // console.log('setLocalDescription', peer.localDescription);
            socket.emit('exchange', {'to': fromId, 'sdp': peer.localDescription });
          }, logError);
        }, logError);
    }, logError);
  } else {
    // console.log('exchange candidate', data);
    peer.addIceCandidate(new RTCIceCandidate(data.candidate));
  }
}

export const leave = (socketId, pcPeers) =>{
  ;
  const peer = pcPeers[socketId];
  // const viewIndex = pc.viewIndex;
  peer && peer.close();
  delete pcPeers[socketId];
  
  // const remoteList = container.state.remoteList;
  // delete remoteList[socketId]
  
}

export const requestPermission = () => {
  if (InCallManager.recordPermission !== 'granted') {
    InCallManager.requestRecordPermission()
    .then((requestedRecordPermissionResult) => {
        this.setState({textLog: requestedRecordPermissionResult})
        console.log("InCallManager.requestRecordPermission() requestedRecordPermissionResult: ", );
    })
    .catch((err) => {
      this.setState({textLog: 'err' + err})
        console.log("InCallManager.requestRecordPermission() catch: ", err);
    });  
  }
}


function logError(error) {
  console.log("logError", error);
}