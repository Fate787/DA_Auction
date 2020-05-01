
Auctions
Auctions is a Bukkit/Spigot plugin that allows players on servers to auction off their items for money. Current features include:

Sealed auctions
Herochat channel integration
Broadcasts through the action bar
Auction queues
Spam reduction methods
Auctions is open source.

Compiling
This fork is written for Java 8 and can be compiled through the use of Gradle. To compile this fork:

Install Gradle
Clone the repository using git clone https://github.com/Kamilkime/Auctions-1.13.2.git
Compile with gradle using gradle shadowJar
Usage
The subcommands for this plugin are as follows:

/auctions start <amount> <price> [increment] [autowin]
/auctions info
/auctions bid <amount>
/auctions end
/auctions cancel
/auctions impound
/auctions ignore
/auctions spam
/auctions queue
/auctions toggle
/auctions reload
