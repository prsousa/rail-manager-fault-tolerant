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
	1. replica: `./run RailManagerServer <srvName> [isFirst]`
	2. remote client: `./run TestRemote`
	3. local client: `./run TestLocal`


## Authors
* Helder Novais - a64378
* João Fernandes - a64341
* Paulo Sousa - pg27774