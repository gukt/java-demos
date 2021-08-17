/* 
 * Copyright 2009 The Corner Team.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bafeimao.examples.network.rpc.server.impl;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.bafeimao.examples.network.rpc.RpcProto;
import net.bafeimao.examples.network.rpc.codec.mina.ProtobufDecoder;
import net.bafeimao.examples.network.rpc.codec.mina.ProtobufEncoder;
import net.bafeimao.examples.network.rpc.server.RpcServer;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message.Builder;

/**
 * RPC Server
 * 
 * @author <a href="mailto:jun.tsai@gmail.com">Jun Tsai</a>
 * @version $Revision$
 * @since 0.1
 */
public class RpcServerImpl implements RpcServer {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcServerImpl.class);
	private SocketAcceptor acceptor;
	private String host;
	private int port;
	private IoHandler ioHandler;

	public RpcServerImpl(String host, int port, IoHandler rpcIoHandler) {
		this.host = host;
		this.port = port;
		this.ioHandler = rpcIoHandler;
	}

	public void start() throws IOException {
		logger.info("starting rpc server!");
		// ExecutorService executor = Executors.newCachedThreadPool();
		int processorCount = Runtime.getRuntime().availableProcessors();
		acceptor = new NioSocketAcceptor(processorCount);
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.getSessionConfig().setReceiveBufferSize(1024);
		acceptor.getSessionConfig().setSendBufferSize(1024);
		acceptor.getSessionConfig().setTcpNoDelay(true);
		acceptor.getSessionConfig().setSoLinger(-1);
		acceptor.setBacklog(10240);

		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		// chain.addLast("executor", new ExecutorFilter(executor));
		/*
		 * chain.addLast("codec", new ProtocolCodecFilter(
		 * ProtobufMessageEncoder.class, ProtobufRequestDecoder.class));
		 */
		addProtobufCodec(chain);
		acceptor.setHandler(ioHandler);
		acceptor.bind(new InetSocketAddress(host, port));
	}

	private void addProtobufCodec(DefaultIoFilterChainBuilder chain) {
		chain.addLast("protobuf", new ProtocolCodecFilter(new ProtobufEncoder(), new ProtobufDecoder() {
			@Override
			protected Builder newBuilder() {
				return RpcProto.Request.newBuilder();
			}
		}));
	}

	/**
	 * @see net.bafeimao.examples.network.rpc.server.RpcServer#stop()
	 */
	public void stop() {
		logger.info("stoping rpc server!");
		acceptor.unbind();
	}
}
