//regex for matching a name string
const nameRegex = RegExp("^(?!-)(?!.*-{2})[-a-zA-Z]{1,50}(?<!-)$");

console.log("Starting program!");
nameInputPhase();
console.log("Moving to Integer phase!");

//function that goes through the first and last name inputs
function nameInputPhase () {
    console.log("Please enter a name");
    console.log("Max 50 characters, alphabetic characters, and dashes only. Dashes cannot be chained.");
    console.log("Name cannot begin or end with a dash.");
    console.log("Apologies that if you are Elon Musk's kid, you cannot use this service.");

    //had to install prompt modules using npm install prompt-sync
    //accept user input for first name
    let firstName = prompt("Enter a first name:");

    //if the user inputs a false name, prompt user for name again
    while (!nameValidator(firstName) | firstName == null) {
        firstName = prompt("Please enter another first name");
    }

    //if user input matches a name continue on in the program
    console.log("First name" + firstName + "has been accepted!");

    //accept user input for last name
    let lastName = prompt("Enter a last name:");

    //if the user inputs a false name, prompt user for name again
    while (!nameValidator(firstName) | lastName == null) {
        lastName = prompt("Please enter another last name");
    }

    //if user input matches a name continue on in the program
    console.log("First name" + lastName + "has been accepted!");
}

//validates a string as a name using nameRegex
function nameValidator(name) {
    if (!nameRegex.exec(name)) {
        console.log();("Invalid Name inputted!");
        return false;
    } else {
        return true;
    }
}

