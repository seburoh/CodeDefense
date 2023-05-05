const fs = require('fs');
const scn = require('prompt-sync')({sigint: true});
//regex for matching a name string
const nameRegex = RegExp("^(?!-)(?!.*-{2})[-a-zA-Z]{1,50}(?<!-)$");

var ErrorLog = {
    logData : "",
    append : function(str) {
        this.logData += str + '\n';
    }
};

var InputData = {
    firstName : "",
    lastName : "",
    firstInt : "",
    secondInt : "",
    hashedPasswordPath : "",
    inputFilePath : "",
    inputFileData : "",
    outputFilePath : "",
    currentTime : "",
    storedSalt : "",
    addNumbers : function() {
        return firstInt + secondInt;
    },
    multiplyNumbers : function() {
        return firstInt * secondInt;
    },
    toString : function() {
        return "First Name: " + firstName + '\n' +
        "Last Name: " + lastName + '\n' +
        "Added Numbers: " + addNumbers() + '\n' +
        "Multiplied Numbers: " + multiplyNumbers() + '\n' +
        "Input File Contents:\n" + inputFileData + '\n';
    }
};

//public static void main(String[] args) {
InputData.currentTime = Date.now();
//ErrorLog.logData = (InputData.currentTime);
//console.log(ErrorLog.logData);
//console.log("Time is: " + InputData.currentTime);

console.log("Starting program!");
nameInputPhase();
numberInputPhase();
//}

function numberInputPhase () {

    console.log("Enter an integer between values -2147483648 and 2147483647.");
    let num = scn("Enter a number: ");
    //if the user inputs a false name, prompt user for name again
    while (!numberValidator(num)) {
        printFailure("Value not accepted as integer: " + num);
        num = scn("Please try again: ");
    }
    InputData.firstInt = num;
    printSuccess("Integer " + InputData.firstInt + " has been accepted!");

    console.log("Enter an integer between values -2147483648 and 2147483647.");
    num = scn("Enter another number: ");
    //if the user inputs a false name, prompt user for name again
    while (!numberValidator(num)) {
        printFailure("Value not accepted as integer: " + num);
        num = scn("Please try again: ");
    }
    InputData.secondInt = num;
    printSuccess("Integer " + InputData.secondInt + " has been accepted!");
}

function numberValidator (num) {
    return Number.isInteger(parseInt(num)) && num < 2147483648 && num > -2147483649; 
}

//function that goes through the first and last name inputs
function nameInputPhase () {
    ErrorLog.append("Reached first name phase");
    console.log("Please enter a name");
    console.log("Max 50 characters in first name and last name each.");
    console.log("Alphabetic characters, and dashes only. Dashes cannot be chained.");
    console.log("Name cannot begin or end with a dash.");
    console.log("Apologies that if you are Elon Musk's kid, you cannot use this service.");
    //had to install prompt modules using npm install prompt-sync
    //accept user input for first name
    let firstName = scn("Enter a first name: ");
    //if the user inputs a false name, prompt user for name again
    while (!nameValidator(firstName)) {
        printFailure("Regex did not match for first name: " + firstName);
        firstName = scn("Please enter another first name: ");
    }
    InputData.firstName = firstName;
    printSuccess("First name " + firstName + " has been accepted!");

    //accept user input for last name
    ErrorLog.append("Reached last name phase");
    console.log("Max 50 characters in first name and last name each.");
    console.log("Alphabetic characters, and dashes only. Dashes cannot be chained.");
    console.log("Name cannot begin or end with a dash.");
    console.log("Apologies that if you are Elon Musk's kid, you cannot use this service.");
    let lastName = scn("Enter a last name: ");
    while (!nameValidator(lastName)) {
        printFailure("Regex did not match for last name: " + lastName);
        lastName = scn("Please enter another last name: ");
    }
    InputData.lastName = lastName;
    printSuccess("Last name " + lastName + " has been accepted!");
}

//validates a string as a name using nameRegex
function nameValidator(name) {
    return nameRegex.exec(name);
}

function printSuccess(message) {
    console.log("SUCCESS: " + message);
    ErrorLog.append("SUCCESS: " + message);
}

function printFailure(message) {
    console.log("ERROR: " + message);
    ErrorLog.append("ERROR: " + message);
}

function writelogFile() {
    
    //try {
        fs.writeFile('logfile_' + InputData.currentTime+'.txt','Log File #' + InputData.currentTime + '\n' + ErrorLog.logData, err => {
            if (err) {
                ErrorLog.append("ERROR:", e.stack);
                return false;
            }
            else console.log('Data written to file successfully.');
        });

    //     return true;
    // } catch(e) {
    //     ErrorLog.append("ERROR", e.stack)
    //     return false;
    // }

    // fs.writeFile('logfile' + InputData.currentTime+'.txt', 'Log File #' + InputData.currentTime, err => {
    //     if (err) console.error(err);
    //     else console.log('Data written to file successfully.');
    // });

    // fs.writeFile()

}
