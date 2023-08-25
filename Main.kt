class ATM {
    private val availableNotes = mutableMapOf(
        2000 to 10,
        500 to 20,
        200 to 30,
        100 to 50
    )

    fun displayAvailableNotes() {
        println("Current available notes in the ATM:")
        availableNotes.forEach { (denomination, count) ->
            println("$denomination - $count notes")
        }
        println()
    }

    fun deposit(denominationCounts: Map<Int, Int>) {
        val allowedDenominations = setOf(2000, 500, 200, 100)
        var totalDeposited = 0

        for ((denomination, count) in denominationCounts) {
            if (!allowedDenominations.contains(denomination)) {
                println("Invalid denomination for deposit: $denomination")
                continue
            }

            availableNotes[denomination] = availableNotes.getOrDefault(denomination, 0) + count
            totalDeposited += count
        }

        if (totalDeposited > 0) {
            println("Deposited total of $totalDeposited notes.\n")
        }
    }


    fun withdraw(withdrawalAmount: Int) {
        var remainingAmount = withdrawalAmount
        println("Withdrawing amount: $withdrawalAmount")
        val withdrawalNotes = mutableMapOf<Int, Int>()

        if (remainingAmount > getTotalAvailableAmount()) {
            println("Insufficient funds in the ATM to fulfill the withdrawal request.\n")
            return
        }

        for (denomination in arrayOf(2000, 500, 200, 100)) {
            val availableCount = availableNotes.getOrDefault(denomination, 0)
            val neededCount = remainingAmount / denomination

            val countToWithdraw = if (availableCount >= neededCount) neededCount else availableCount

            if (countToWithdraw > 0) {
                withdrawalNotes[denomination] = countToWithdraw
                availableNotes[denomination] = availableCount - countToWithdraw
                remainingAmount -= countToWithdraw * denomination
            }
        }

        if (remainingAmount == 0) {
            println("Withdrawn denominations:")
            withdrawalNotes.forEach { (denomination, count) ->
                println("$count notes of $denomination")
            }
            println("Withdrawal successful.\n")
        } else {
            println("Insufficient denominations to fulfill the withdrawal request.\n")
        }
    }

    private fun getTotalAvailableAmount(): Int {
        return availableNotes.entries.sumOf { (denomination, count) -> denomination * count }
    }
}


fun main() {
    val atm = ATM()

    while (true) {
        println("ATM Menu:")
        println("1. Display available notes")
        println("2. Deposit money")
        println("3. Withdraw money")
        println("4. Exit")
        print("Enter your choice: ")

        val choice = readln().toIntOrNull() ?: continue

        when (choice) {
            1 -> atm.displayAvailableNotes()
            2 -> {
                val denominationCounts = mutableMapOf<Int, Int>()
                for (denomination in setOf(2000, 500, 200, 100)) {
                    print("Enter count of $denomination notes: ")
                    val count = readln().toIntOrNull() ?: continue
                    if (count > 0) {
                        denominationCounts[denomination] = count
                    }
                }
                if (denominationCounts.isNotEmpty()) {
                    atm.deposit(denominationCounts)
                } else {
                    println("No valid notes deposited.\n")
                }
            }
            3 -> {
                print("Enter amount to withdraw: ")
                val amount = readln().toIntOrNull() ?: continue
                atm.withdraw(amount)
            }
            4 -> {
                println("Exiting ATM. Have a nice day!")
                return
            }
            else -> println("Invalid choice. Please select a valid option.\n")
        }
    }
}