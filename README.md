# Fault Tolerant Rail Manager Server

Details at `enunciado.pdf`.

## Features

* Active Replication (servers)
* Communication protocol using [spread](http://www.spread.org/).
* 

## Usage

1. Start `spread` server on `localhost` at port `4803`.
2. `mvn compile` to compile
3. Run
	1. replica: `./runUnix RailManagerServer <srvName> [isFirst]`
	2. remote client: `./runUnix TestRemote`
	3. local client: `./runUnix TestLocal`


## Authors
* Helder Novais - a64378
* João Fernandes - a64341
* Paulo Sousa - pg27774
