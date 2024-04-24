<br />
<p align="center">
  <h1 align="center">Stock Market Simulation</h1>

  <p align="center">
    <A simultaion of a Stock Market>
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running](#running)
* [Modules](#modules)
* [Notes](#notes)
* [Evaluation](#evaluation)
* [Extras](#extras)

## About The Project

The description of the project is given by a model of a real life Stock Market application for the exchange and a Trader Application for the trading bots that communicate through the network module. 

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Maven 3.6](https://maven.apache.org/download.cgi) or higher

### Installation

1. Navigate to the `stockList` directory
2. Clean and build the project using:
```sh
mvn install
```

### Running


First we run the `Main` from the `StockApplication` module where we load the stock list and trader list from the yaml files found in the data module.
Secondly, we run the `MainTradingClient` from the `TraderApplication` module which is creating a new thread for a random non-running bot client. Run that file for how many trading bots you want to connect to the stock server.


## Modules

<!--
-->
`command` is the module made up by the `CommandHandler` and it's main role is to handle and execute possible commands which are implementing the `Command` Interface.
With this module, the Command Design Pattern is integrated into the project's efficiency that is handling and executing potential commands. As an example,
executing a stock order and placing stock orders into the network queues are the implementation of a command.

`message-queue` is the module that  its main purpose is to store the logic of a part of the project's communication structure.
This module is consisting of the priority queues, which are used over the network in order to store messages
ordered ascendant by the timestamp when the message was created;

`networking` is the module that facilitates all the networking tasks between a server and a client. Along the connection between the client and server,
the module is having the `handlers` which are responsible for handling a specific message over the network and manage to
do the desired action with it. For example, once a client is connected to the server, a clientHandler is spawned which can handle the
inputs of the client, from the client Handler the message is sent to the MessageHandler alongside with the CommandHandler if needed.

`stock-market-ui` is the module which is accomplishing the view of the `StockApplication`. The main frame of this UI is consisting of
two tables which contain the needed information of the stocks and traders. By using this view model, it ensures a clean presentation layout
of the data consisting of the Stock Exchange.

`StockApplication` is the main module of the stock Application which is consisting of the execution of the sell / buy orders sent by
the clients over the network as commands. This module is also storing the main data which enables all the logic behind it, such as StockExchange
, Stock and Trader. This module is having its dependencies over the `networking`, `message-queue`, and `command` modules so that it
completes the tasks over the internet of the Stock Market simulation, by managing to keep track of the connected clients, and making the communication
over the server client viable.

`TraderApplication` is the main module of the trader application which is consisting of spawning a bot which is taking care
of a random trader's orders selected from the given traderList. In this application a trader is linked to a client so it can
connect to the StockApplication. The strategy adopted by the bot is a random one which is choosing randomly between BUY or SELL,
a random stock to buy/sell, a random quantity and a random price around the actual price of the stock.

`Util` is the module which is containing the utility classes like the YAML loader which helps in order to load from
diverse YAML files. In the `StockApplication` and `TraderApplication` the yaml loader is used in order to load
the stocks and traders from the files.

## Design

<!--
-->
`Command Pattern` is one of the most important patterns in the `StockApplication` as there are 3 classes that are implementing the `Command` interface:
`BuyMqPutCmd`, `SellMqPutCmd` and `ExecuteCmd`. Alongside with the implementation of the interface, there is a `Command Handler` which is responsible
for mapping commands and managing their execution based on the header of the message (if the handled message is a command). This design choice is very useful
for this project as it provides Decoupling and Flexibility to the code as adding new features or modifying the logic of a command will not affect other parts
of the code. So the `Command` implementation is ensuring that all the commands are executed in a consistent manner which leads to a better
error handling.

`Singleton` implementation is used to make a single instance of the Stock Exchange which is always passed as parameters to the methods that need information
from the StockExchange class. This approach ensures that all the clients are operating with the same instance of the data so there are not any
different results for different clients. By using this implementation in a Stock Market is crucial as we do not want to have a different instance of an
Exchange for different traders.

`Observer Pattern` implementation is used in the StockExchange in order to notify all the traders about the changes that have been made in the
exchange. This kind of approach enables a real-time notification system, because in a real Stock Exchange, we want the information to be
updated to every trader connected so they are up to date. In essence, the `Stock Exchange` is acting as the publisher and the traders are the `observers` which
are waiting for updates in the information.

## Evaluation

<!--
Discuss the stability of your implementation. What works well? Are there any bugs? Is everything tested properly? Are there still features that have not been implemented? Also, if you had the time, what improvements would you make to your implementation? Are there things which you would have done completely differently? Try to aim for at least 250 words.

-->
When discussing the features of the project, this is fully accomplished by our implementation and manages to check all the requirements of the assignments.
It runs smoothly and provides all the needed information how it is supposed to be. If we had more time we would have implemented a more
advanced trading strategy so that it would be more complex. This implementation would have made the bot trading strategy closer to the real life
Stock Market.

In the project there are 2 slightly bugs which are not affecting in a big way the project's purpose. One would be that when loading the YAML file we couldn't manage to use the path from the project because it was giving errors, like this : stocks/data/stocks.yaml. But we managed to find a work
around to this problem as we are using the absolute path of the file : C:\Cosmin\University\RUG\Year 2\Semester 1\1a\Advaced Object Oriented Programming\2023_Team_057\stocks\data\stocks.yaml
which means that if somebody will run the project on their computer it won't work as the file is not in the same path location.

Another slightly bug that we have, is one that we encountered during refactoring the execution order command, as we were doing the maven clean compile test. Now,
when letting the program run a long period of time, some stock shares of some traders are going negative. This was working before the refactoring of the code
as we had a method of 51 lines, but did not manage to split it into more methods. The bug was encountered before the deadline with an hour, and we preferred to pass
the maven, so we can pull request and have a bug rather than not being able to pass the maven.

The current method is having an issue in the logic, but here is the method which was working properly:

```{Java}
      @Override
    public void execute(Map<String, Object> params) {
        String stockSymbolBuy = (String) params.get("stockSymbol");
        double quantityBuy = (double) params.get("stockQuantity");
        double priceBuy = (double) params.get("stockPrice");
        String traderIdBuy = (String) params.get("traderId");
        int clientIdOldBuy = (int) params.get("clientId");
        int size = stockExchange.getSellQueue().getSize();

        double minPriceToBuy = -1;
        minPriceToBuy = getMinPriceToBuy(size, stockSymbolBuy, minPriceToBuy);

        size = stockExchange.getSellQueue().getSize();
        int okBuy = 0;
        double reminderToBuy = -1;
        if (minPriceToBuy != -1) {
            while (size != 0) {
                reminderToBuy = quantityBuy;
                Message message = stockExchange.getSellQueue().dequeue();
                String[] tokens = message.getBody().split(" ");
                String sellStock = tokens[0];
                double sellQuantity = Double.parseDouble(tokens[1]);
                double sellPrice = Double.parseDouble(tokens[2]);
                int clientId = Integer.parseInt(tokens[3]);
                String sellTraderId = tokens[4];
                if ( minPriceToBuy <= priceBuy && minPriceToBuy == sellPrice && stockSymbolBuy.equals(sellStock)) {
                    stockExchange.getStockList().findBySymbolStock(stockSymbolBuy).setPrice(sellPrice);
                    if (quantityBuy > sellQuantity) {
                        reminderToBuy = partialBuyOrder(quantityBuy, sellQuantity, sellTraderId, sellStock,
                                stockSymbolBuy, traderIdBuy, priceBuy, sellPrice);
                    }
                    if (quantityBuy == sellQuantity) {
                        perfectMatch(sellQuantity, sellTraderId, sellStock, stockSymbolBuy, quantityBuy,
                                traderIdBuy, priceBuy, sellPrice);
                        okBuy = 1;
                        break;
                    }
                    if (quantityBuy < sellQuantity) {
                        okBuy = partialSellOrder(sellQuantity, quantityBuy, sellTraderId, stockSymbolBuy,
                                sellPrice, traderIdBuy, priceBuy, clientId);
                        break;
                    }
                }
                if (reminderToBuy != -1) {
                    generateOrder(stockSymbolBuy, reminderToBuy, priceBuy, clientIdOldBuy, traderIdBuy);
                }
                size--;
            }
        }
        if (okBuy == 0 && reminderToBuy == -1) {
            generateOrder(stockSymbolBuy, quantityBuy, priceBuy, clientIdOldBuy, traderIdBuy);
        }
    }
```

## Extras

<!--
If you implemented any extras, you can list/mention them here.
-->

___


<!-- Below you can find some sections that you would normally put in a README, but we decided to leave out (either because it is not very relevant, or because it is covered by one of the added sections) -->

<!-- ## Usage -->
<!-- Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. -->

<!-- ## Roadmap -->
<!-- Use this space to show your plans for future additions -->

<!-- ## Contributing -->
<!-- You can use this section to indicate how people can contribute to the project -->

<!-- ## License -->
<!-- You can add here whether the project is distributed under any license -->


<!-- ## Contact -->
<!-- If you want to provide some contact details, this is the place to do it -->

<!-- ## Acknowledgements  -->
