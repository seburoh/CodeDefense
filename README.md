# CodeDefense
TCSS 483 Code Defense Exercise completed in Java and NodeJS.
Packages required for NodeJS are already included. Both source files are contained in the /src folder and are self-reliant.

## Inputs and Relevant Defenses
* Name
    * Cannot be longer than 50 characters.
    * Can only include english alphabetic characters a-z, case insensitive, and hyphens.
    * Names cannot begin or end with hyphens, and two or more hyphens cannot appear in a row (ex: a-, -a, a--a).
* Number
    * Accepts only plain integer value within the range of -2147483648 to 2147483647.
    * Input numbers cannot contain decimals, they must be actual plain integers. Even if it would result in an integer value logically, for example 2.0 is not allowed.
    * Is able to add and multiply input numbers together and not overflow through careful consideration of value types and their relevant bounds.
* Input File
    * Accepts paths to valid .txt files from the user to draw content from.
    * Able to locate files through relative and absolute paths.
    * Utilizes built in functionality from Java and NodeJS to ensure file is valid for user to access.
* Output File
    * Accepts paths to place a NEW .txt file at for program output.
    * The user must type in the .txt extension as part of the input.
    * Path input that lead to existing files are rejected, including to the specified input file from the previous step.
    * File path must include at least one character more than the .txt extension of the file path itself.
    * Filename validity is handled by included Java and NodeJS libraries. This means that technically correct but realistically stupid filenames like ").txt" and ".txt.txt" are allowed. After all, technically correct is the best kind of correct.
    * It is purposefully up to the user to decide what is a responsible filename, beyond what is allowed by the host system.
* Password
    * Password must be at least 13 characters in length.
    * Must include at least one uppercase letter, lowercase letter, and number.
    * May optionally include any number of the following characters: !@#
    * No character present in the password may be repeated more than twice in a row. "aa" is ok, "aaa" is not.
    * Passwords are hashed and salted using SHA256, with securely generated random salt.
    * The salt is unique per each run of the program and is not saved after runtime completes.
    * The second time the user enters the password, it is salted and hashed, then compared against the first successful input, which is read in from a file specified in the Outputs section of the readme.

## Outputs
* User-named output file
    * Contains one item per line:
        * User's first name.
        * User's last name.
        * Sum of both numbers entered.
        * Product of both numbers entered.
        * Contents of input file specified.
* Log file
    * Log files are named log_TIME.txt, where TIME is the system time in milliseconds when program is started. This ensures a unique logfile name for each run of the program.
    * Contains logs from runtime, including all generated error messages as well as markers for when users successfully reached specific milestones of progress.
* Hash file
    * Hash files are named similar to Log files. hash_TIME.txt, where TIME is the system time in milliseconds when program is started.
    * Contains the first successful password input from the user, after it has been salted and hashed.

<img src="https://github.com/cat-milk/Anime-Girls-Holding-Programming-Books/blob/master/NodeJs/Node_JS_In_Action.jpg?raw=true" alt="Italian anime">
