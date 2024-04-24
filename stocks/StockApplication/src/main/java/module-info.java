module StockApplication {
    requires messagequeue;
    requires command;
    requires networking;
    requires lombok;
    requires stock.market.ui;
    requires util;
    requires awaitility;
    requires org.slf4j;
    exports nl.rug.aoop.trader to com.fasterxml.jackson.databind, TraderApplication;
    exports nl.rug.aoop.stock to com.fasterxml.jackson.databind, TraderApplication;
}