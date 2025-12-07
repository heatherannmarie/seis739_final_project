CREATE TABLE IF NOT EXISTS Parents (
    parentId INT NOT NULL PRIMARY KEY,
    parentName VARCHAR(258) NOT NULL
);

CREATE TABLE IF NOT EXISTS Children (
    childId INT NOT NULL PRIMARY KEY,
    childName VARCHAR(258) NOT NULL,
    childUserName VARCHAR(258) NOT NULL,
    parentId INT NOT NULL,
    accountBalance DECIMAL(12, 2) DEFAULT 0.00,
    FOREIGN KEY (parentId) REFERENCES Parents
);

CREATE TABLE IF NOT EXISTS Chores (
    choreId INT NOT NULL PRIMARY KEY,
    choreName VARCHAR(258) NOT NULL,
    choreDescription VARCHAR(1000) NOT NULL,
    childId INT,
    parentId INT NOT NULL,
    isAvailable BOOLEAN NOT NULL,
    FOREIGN KEY (parentId) REFERENCES Parents,
    FOREIGN KEY (childId) REFERENCES Children
);

CREATE TABLE IF NOT EXISTS Transactions (
    transactionId INT NOT NULL PRIMARY KEY,
    childId INT NOT NULL,
    parentId INT NOT NULL,
    transactionDescription VARCHAR(1000) NOT NULL,
    transactionAmount DECIMAL(12, 2) NOT NULL,
    transactionTime TIMESTAMP NOT NULL,
    FOREIGN KEY (parentId) REFERENCES Parents,
    FOREIGN KEY (childId) REFERENCES Children
);

CREATE TABLE IF NOT EXISTS StoreItems (
    parentId INT NOT NULL,
    itemName VARCHAR(258) NOT NULL,
    itemPrice DECIMAL(12, 2) DEFAULT 0.00,
    itemId INT NOT NULL PRIMARY KEY,
    FOREIGN KEY (parentId) REFERENCES Parents
);