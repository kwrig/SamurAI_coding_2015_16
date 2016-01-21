# -*- coding: utf-8 -*-
"""
Deep Q-network implementation with chainer and rlglue
Copyright (c) 2015  Naoto Yoshida All Right Reserved.
"""

import copy

import numpy as np
import sys
import os

from chainer import FunctionSet, Variable, optimizers, serializers, cuda
import chainer.functions as F


class DQN_class:
    # Hyper-Parameters
    gamma = 0.99  # Discount factor
    initial_exploration = 100  # 10**4  # Initial exploratoin. original: 5x10^4
    replay_size = 32  # Replay (batch) size
    target_model_update_freq = 10 ** 4  # Target update frequancy. original: 10^4
    data_size = 10 ** 5  # Data size of history. original: 10^6

    field_num = 7
    field_size = 17

    def __init__(self, control_size=10, field_num=7, field_size=17):
        self.num_of_actions = control_size
        self.field_size = field_size

        # self.enable_controller = enable_controller  # Default setting : "Pong"


        print "Initializing DQN..."
        #	Initialization of Chainer 1.1.0 or older.
        # print "CUDA init"
        # cuda.init()

        self.field_num = field_num

        print "Model Building"
        self.model = FunctionSet(
                l1=F.Convolution2D(self.field_num * 4, 16, ksize=5, stride=1, nobias=False, wscale=np.sqrt(2)),
                l2=F.Convolution2D(16, 24, ksize=4, stride=1, nobias=False, wscale=np.sqrt(2)),
                l3=F.Linear(2400, 512, wscale=np.sqrt(2)),
                q_value=F.Linear(512, self.num_of_actions,
                                 initialW=np.zeros((self.num_of_actions, 512),
                                                   dtype=np.float32))
        ).to_gpu()

        self.model_target = copy.deepcopy(self.model)

        print "Initizlizing Optimizer"
        self.optimizer = optimizers.RMSpropGraves(lr=0.00025, alpha=0.95, momentum=0.95, eps=0.0001)
        # self.optimizer.setup(self.model.collect_parameters())
        self.optimizer.setup(self.model)

        # History Data :  D=[s, a, r, s_dash, end_episode_flag]
        self.D = [np.zeros((self.data_size, self.field_num * 4, self.field_size, self.field_size), dtype=np.uint8),
                  np.zeros(self.data_size, dtype=np.uint8),
                  np.zeros((self.data_size, 1), dtype=np.int8),
                  np.zeros((self.data_size, self.field_num * 4, self.field_size, self.field_size), dtype=np.uint8),
                  np.zeros((self.data_size, 1), dtype=np.bool)]

    def forward(self, state, action, Reward, state_dash, episode_end):
        num_of_batch = state.shape[0]
        s = Variable(state)
        s_dash = Variable(state_dash)

        Q = self.Q_func(s)  # Get Q-value

        # Generate Target Signals
        tmp = self.Q_func_target(s_dash)  # Q(s',*)
        tmp = list(map(np.max, tmp.data.get()))  # max_a Q(s',a)
        max_Q_dash = np.asanyarray(tmp, dtype=np.float32)
        target = np.asanyarray(Q.data.get(), dtype=np.float32)

        for i in xrange(num_of_batch):
            if not episode_end[i][0]:
                tmp_ = np.sign(Reward[i]) + self.gamma * max_Q_dash[i]
            else:
                tmp_ = np.sign(Reward[i])

                # action_index = self.action_to_index(action[i])
            target[i, action[i]] = tmp_

        # TD-error clipping


        td = Variable(cuda.to_gpu(target)) - Q  # TD error
        # td = Variable(target) - Q  # TD error


        td_tmp = td.data + 1000.0 * (abs(td.data) <= 1)  # Avoid zero division
        td_clip = td * (abs(td.data) <= 1) + td / abs(td_tmp) * (abs(td.data) > 1)

        zero_val = Variable(cuda.to_gpu(np.zeros((self.replay_size, self.num_of_actions), dtype=np.float32)))
        # zero_val = Variable(np.zeros((self.replay_size, self.num_of_actions), dtype=np.float32))



        loss = F.mean_squared_error(td_clip, zero_val)
        return loss, Q

    def stockExperience(self, time,
                        state, action, reward, state_dash,
                        episode_end_flag):
        data_index = time % self.data_size

        if episode_end_flag is True:
            self.D[0][data_index] = state
            self.D[1][data_index] = action
            self.D[2][data_index] = reward
        else:
            self.D[0][data_index] = state
            self.D[1][data_index] = action
            self.D[2][data_index] = reward
            self.D[3][data_index] = state_dash
        self.D[4][data_index] = episode_end_flag

    def experienceReplay(self, time):

        if self.initial_exploration < time:
            # Pick up replay_size number of samples from the Data
            if time < self.data_size:  # during the first sweep of the History Data
                replay_index = np.random.randint(0, time, (self.replay_size, 1))
            else:
                replay_index = np.random.randint(0, self.data_size, (self.replay_size, 1))

            s_replay = np.ndarray(shape=(self.replay_size, self.field_num * 4, self.field_size, self.field_size),
                                  dtype=np.float32)
            a_replay = np.ndarray(shape=(self.replay_size, 1), dtype=np.uint8)
            r_replay = np.ndarray(shape=(self.replay_size, 1), dtype=np.float32)
            s_dash_replay = np.ndarray(shape=(self.replay_size, self.field_num * 4, self.field_size, self.field_size),
                                       dtype=np.float32)
            episode_end_replay = np.ndarray(shape=(self.replay_size, 1), dtype=np.bool)
            for i in xrange(self.replay_size):
                s_replay[i] = np.asarray(self.D[0][replay_index[i]], dtype=np.float32)
                a_replay[i] = self.D[1][replay_index[i]]
                r_replay[i] = self.D[2][replay_index[i]]
                s_dash_replay[i] = np.array(self.D[3][replay_index[i]], dtype=np.float32)
                episode_end_replay[i] = self.D[4][replay_index[i]]

            s_replay = cuda.to_gpu(s_replay)
            s_dash_replay = cuda.to_gpu(s_dash_replay)
            ##修正なし



            # Gradient-based update
            self.optimizer.zero_grads()
            loss, _ = self.forward(s_replay, a_replay, r_replay, s_dash_replay, episode_end_replay)
            loss.backward()
            self.optimizer.update()

    def Q_func(self, state):
        h1 = F.relu(self.model.l1(state / 254.0))  # scale inputs in [0.0 1.0]
        h2 = F.relu(self.model.l2(h1))
        h3 = F.relu(self.model.l3(h2))
        Q = self.model.q_value(h3)
        return Q

    def Q_func_target(self, state):
        h1 = F.relu(self.model_target.l1(state / 254.0))  # scale inputs in [0.0 1.0]
        h2 = F.relu(self.model_target.l2(h1))
        h3 = F.relu(self.model_target.l3(h2))
        Q = self.model_target.q_value(h3)
        return Q

    def e_greedy(self, state, epsilon):
        s = Variable(state)
        Q = self.Q_func(s)
        Q = Q.data

        if np.random.rand() < epsilon:
            index_action = np.random.randint(0, self.num_of_actions)
            print "RANDOM"
        else:
            index_action = np.argmax(Q.get())
            print "GREEDY"
        return index_action, Q

    def target_model_update(self):
        self.model_target = copy.deepcopy(self.model)

    def save_model(self, model_name, opt_name):
        serializers.save_hdf5(model_name, self.model)
        serializers.save_hdf5(opt_name, self.optimizer)

    def read_model(self, model_name, opt_name):
        serializers.load_hdf5(model_name, self.model)
        serializers.load_hdf5(opt_name, self.optimizer)
        self.model_target = copy.deepcopy(self.model)


"""
    def index_to_action(self, index_of_action):
        return self.enable_controller[index_of_action]

    def action_to_index(self, action):
        return self.enable_controller.index(action)
"""


class dqn_agent:  # RL-glue Process
    lastAction = 0
    policyFrozen = False

    field_num = 1
    control_size = 12

    field_size = 17

    def agent_init(self):
        # Some initializations for rlglue
        self.lastAction = 0

        self.time = 0
        self.epsilon = 1.0  # Initial exploratoin rate

        # Pick a DQN from DQN_class

        os.environ["PATH"] += ':/Developer/NVIDIA/CUDA-7.5/bin'

        print os.environ["PATH"]

        self.DQN = DQN_class(control_size=self.control_size, field_num=self.field_num,
                             field_size=self.field_size)  # default is for "Pong".

    def agent_start(self, observation):

        # Preprocess
        # tmp = np.bitwise_and(np.asarray(observation.intArray[128:]).reshape([210, 160]), 0b0001111)  # Get Intensity from the observation
        # obs_array = (spm.imresize(tmp, (110, 84)))[110-84-8:110-8, :]  # Scaling

        tmp = np.array(observation)
        obs_array = tmp.reshape(self.field_size, self.field_size, self.field_num)

        # Initialize State
        self.state = np.zeros((self.field_num * 4, self.field_size, self.field_size), dtype=np.uint8)

        for var in range(0, self.field_num):
            self.state[var] = obs_array[var]
        # self.state[0] = obs_array

        state_ = cuda.to_gpu(np.asanyarray(self.state.reshape(1, self.field_num * 4, self.field_size, self.field_size),
                                           dtype=np.float32))
        # state_ = np.asanyarray(self.state.reshape(1, self.field_num * 4, self.field_size, self.field_size), dtype=np.float32)


        # Generate an Action e-greedy
        action, Q_now = self.DQN.e_greedy(state_, self.epsilon)
        returnAction = action

        # Update for next step
        self.lastAction = action
        self.last_state = self.state.copy()
        self.last_observation = obs_array

        return returnAction

    def agent_step(self, observation , reward):

        # Preproces
        # tmp = np.bitwise_and(np.asarray(observation.intArray[128:]).reshape([210, 160]), 0b0001111)  # Get Intensity from the observation
        # obs_array = (spm.imresize(tmp, (110, 84)))[110-84-8:110-8, :]  # Scaling
        # obs_processed = np.maximum(obs_array, self.last_observation)  # Take maximum from two frames


        tmp = np.array(observation)
        obs_array = tmp.reshape(self.field_size, self.field_size, self.field_num)

        # Compose State : 4-step sequential observation
        next_state = np.zeros((self.field_num * 4, self.field_size, self.field_size), dtype=np.uint8);
        for var in range(0, self.field_num):
            next_state[var] = obs_array[var]

        for var in range(0, self.field_num * 3):
            next_state[var + self.field_num] = self.state[var]

        self.state = np.asanyarray(next_state, dtype=np.uint8)

        state_ = cuda.to_gpu(np.asanyarray(self.state.reshape(1, self.field_num * 4, self.field_size, self.field_size),
                                           dtype=np.float32))
        # state_ = np.asanyarray(self.state.reshape(1, self.field_num*4, self.field_size, self.field_size), dtype=np.float32)


        # Exploration decays along the time sequence
        if self.policyFrozen is False:  # Learning ON/OFF
            if self.DQN.initial_exploration < self.time:
                self.epsilon -= 1.0 / 10 ** 6
                if self.epsilon < 0.1:
                    self.epsilon = 0.1
                eps = self.epsilon
            else:  # Initial Exploation Phase
                print "Initial Exploration : %d/%d steps" % (self.time, self.DQN.initial_exploration)
                eps = 1.0
        else:  # Evaluation
            print "Policy is Frozen"
            eps = 0.05

        # Generate an Action by e-greedy action selection
        action, Q_now = self.DQN.e_greedy(state_, eps)
        returnAction = action

        # Learning Phase
        if self.policyFrozen is False:  # Learning ON/OFF
            self.DQN.stockExperience(self.time, self.last_state, self.lastAction, reward, self.state, False)
            self.DQN.experienceReplay(self.time)

        # Target model update
        if self.DQN.initial_exploration < self.time and np.mod(self.time, self.DQN.target_model_update_freq) == 0:
            print "########### MODEL UPDATED ######################"
            self.DQN.target_model_update()

        # Simple text based visualization
        print ' Time Step %d /   ACTION  %d  /   REWARD %.1f   / EPSILON  %.6f  /   Q_max  %3f' % (
        self.time, action, np.sign(reward), eps, np.max(Q_now.get()))

        # Updates for next step
        self.last_observation = obs_array

        if self.policyFrozen is False:
            self.lastAction = copy.deepcopy(returnAction)
            self.last_state = self.state.copy()
            self.time += 1

        return returnAction

    def agent_end(self, reward):  # Episode Terminated

        # Learning Phase
        if self.policyFrozen is False:  # Learning ON/OFF
            self.DQN.stockExperience(self.time, self.last_state, self.lastAction, reward, self.last_state,
                                     True)
            self.DQN.experienceReplay(self.time)

        # Target model update
        if self.DQN.initial_exploration < self.time and np.mod(self.time, self.DQN.target_model_update_freq) == 0:
            print "########### MODEL UPDATED ######################"
            self.DQN.target_model_update()

        # Simple text based visualization
        print '  REWARD %.1f   / EPSILON  %.5f' % (np.sign(reward), self.epsilon)

        # Time count
        if self.policyFrozen is False:
            self.time += 1

    def agent_cleanup(self):
        pass


if __name__ == "__main__":



    agent = dqn_agent()
    agent.agent_init()
    action = 0;

    print 'inputWait'

    while True:

        sys.stdout.flush()

        n = raw_input()
        if n == 'start':
            l = map(int, raw_input().split())
            action = agent.agent_start(l)
            print 'action ' + str(action)

        elif n == 'next':
            l = map(int, raw_input().split())
            q = input();
            action = agent.agent_step(l, q)
            print 'action ' + str(action)

        elif n == 'end':
            q = input()
            agent.agent_end(q)
        elif n == 'fin':
            print "finish pythonAgent"
            break
        elif n == 'frozen':
            agent.policyFrozen = True

        elif n == 'save':
            model_name = input()
            opt_name = input()
            agent.DQN.save_model(model_name, opt_name)

        elif n == 'load':
            model_name = input()
            opt_name = input()
            agent.DQN.read_model(model_name, opt_name)


        else:
            sys.stderr.write('error input ' + n)
