import React, { Component } from 'react';
import { createStore } from 'redux';
import { guestReducer } from './guest-reducers';


const guestStore = createStore(guestReducer);

export default guestStore;