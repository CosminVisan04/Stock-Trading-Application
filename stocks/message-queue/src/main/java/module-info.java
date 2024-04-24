module messagequeue {
    exports nl.rug.aoop.messagequeue.consumer;
    exports nl.rug.aoop.messagequeue.producer;
    exports nl.rug.aoop.messagequeue.message;
    exports nl.rug.aoop.messagequeue.queue;
    exports nl.rug.aoop.messagequeue.command;
    requires static lombok;
    requires com.google.gson;
    requires command;
}