const scn = require('prompt-sync')({sigint: true});
const crypto = require("crypto");
var fs = require('fs');
const path = require('path');
//regex for matching a name string
const nameRegex = RegExp("^(?!-)(?!.*-{2})[-a-zA-Z]{1,50}(?<!-)$");
const fileRegex = RegExp("^.+\.txt$");

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
        return this.firstInt + this.secondInt;
    },
    multiplyNumbers : function() {
        return this.firstInt * this.secondInt;
    },
    toString : function() {
        return "First Name: " + this.firstName + '\n' +
        "Last Name: " + this.lastName + '\n' +
        "Added Numbers: " + this.addNumbers() + '\n' +
        "Multiplied Numbers: " + this.multiplyNumbers() + '\n' +
        "Input File Contents:\n" + this.inputFileData + '\n';
    }
};

//public static void main(String[] args) {
console.log("Thank you for using Cereal Experiments Weebs Input Validator.");
InputData.currentTime = Date.now();
nameInputPhase();
numberInputPhase();
fileInputPhase();
fileOutputPhase();
passwordInputPhase();
console.log("Weebs are doing their best now with your file content, please watch warmly until it is ready.");
writelogFile();

console.log("The borderland was wrapped in scarlet magic. Weebs believe that you solve this mystery");
//}

function fileInputPhase() {
    ErrorLog.append("Reached Input File Input Phase");
    let fileName = scn("Enter input file path including file name (File must be a .txt): ");

    while (!fileInputValidator(fileName)) {
        printFailure("File unable to be opened with provided path: " + fileName);
        fileName = scn("Enter another file path: ");
    }

    printSuccess("Input file contents have been read.");

}

function fileOutputPhase() {
    ErrorLog.append("Reached Output File Input Phase");
    let fileName = scn("Enter output file path including file name (File must be a .txt): ");

    while (!fileOutputValidator(fileName)) {
        printFailure("File unable to be opened with provided path: " + fileName);
        fileName = scn("Enter another file path: ");
    }

    printSuccess("Output file contents have been written.");
}

function fileOutputValidator(fileName) {
    let data = InputData.toString();

    if (!fileRegex.exec(fileName) | path.resolve(fileName) == InputData.inputFilePath) {
        return false;
    } else  {
        try {
            fs.writeFileSync(fileName, data, {flag: 'wx'});
        } catch (error) {
            return false
        }
    }
 
    return true;
}

function fileInputValidator(fileName) {
    if (!fileRegex.exec(fileName)) {
        return false;
    } else {
        try {
            var f = fs.readFileSync(fileName);
        } catch (error) {
            return false;
        }
    }

    InputData.inputFileData = f;
    InputData.inputFilePath = path.resolve(fileName);
    return true;
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
}

function passwordInputPhase() {
    ErrorLog.append("Reached first password phase");
    InputData.hashedPasswordPath = "hash_" + InputData.currentTime + ".txt";

    console.log("Enter password to hash.");
    console.log("Password must contain an uppercase and lowercase letter, and a number.");
    console.log("Password must be at least 13 characters long, and may optionally contain these symbols: !@#");
    console.log("Password may not have characters repeated 3+ times in a row.");
    console.log("This will be stored in a file beginning with the name hash_.");
    let pw = scn("Password: ");
    //if the user inputs a false name, prompt user for name again
    while (!writePassword(pw, InputData.hashedPasswordPath)) {
        printFailure("Password creation failed: " + pw);
        pw = scn("Please try again: ");
    }
    printSuccess("Password has been accepted!");

    ErrorLog.append("Reached second password phase");
    console.log("Enter password again to compare against previously input password.");
    pw = scn("Password: ");
    while (!comparePassword(pw)) {
        printFailure("Password did not match: " + pw);
        pw = scn("Please try again: ");
    }
    printSuccess("Hashed password successfully compared.");
}

function comparePassword(pw) {
    var first = readPasswordFromFile(InputData.hashedPasswordPath);
    var second = hashPassword(pw);

    return first != null && second != null && first == second;
}

function readPasswordFromFile(path) {
    try {  
        var data = fs.readFileSync(path);
        return data;
    } catch(e) {
        ErrorLog.append("ERROR:", e.stack);
        return null;
    }
}

function writePassword(pw, path) {
    const regex = RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*(.)\\1{2,})[A-Za-z\\d!@#]{13,}$");

    if (pw == null || !regex.exec(pw)) {
        printFailure("Password not strong enough.")
        return false;
    }

    var hashed = hashPassword(pw);

    if (hashed == null) {
        return false;
    }

    try {
        fs.writeFileSync(path, hashed);
    } catch (error) {
        ErrorLog.append("ERROR: ", error.stack);
        return false
    }

    return true;
}

function hashPassword(pw) {
    if (InputData.storedSalt == "") {
        InputData.storedSalt = crypto.randomBytes(pw.length);
    }
    
    let hashedPassword = crypto.createHash("sha256").update(pw + InputData.storedSalt).digest("hex");
    return hashedPassword;
}

function numberInputPhase () {
    ErrorLog.append("Reached first number phase");
    console.log("Enter an integer between values -2147483648 and 2147483647.");
    let num = scn("Enter a number: ");
    //if the user inputs a false name, prompt user for name again
    while (!numberValidator(num)) {
        printFailure("Value not accepted as integer: " + num);
        num = scn("Please try again: ");
    }
    InputData.firstInt = BigInt(num);
    printSuccess("Integer " + InputData.firstInt + " has been accepted!");

    ErrorLog.append("Reached second number phase");
    console.log("Enter an integer between values -2147483648 and 2147483647.");
    num = scn("Enter another number: ");
    //if the user inputs a false name, prompt user for name again
    while (!numberValidator(num)) {
        printFailure("Value not accepted as integer: " + num);
        num = scn("Please try again: ");
    }
    InputData.secondInt = BigInt(num);
    printSuccess("Integer " + InputData.secondInt + " has been accepted!");
}

function numberValidator (num) {
    return Number.isInteger(Number(num)) && num < 2147483648 && num > -2147483649;
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
