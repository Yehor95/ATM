package ua.pti.myatm;

public class ATM {
        
        int indexOfIllegalInput;
        double moneyInATM;
        Card currentCard;
        Card previousCard;
       
	ATM(double moneyInATM) throws IllegalArgumentException{
            if(moneyInATM < 0) throw new IllegalArgumentException();
            this.moneyInATM = moneyInATM;
            this.indexOfIllegalInput = 0;
            this.previousCard = null;
            this.currentCard = null;
	}
        
        ATM(Card card, double moneyInATM){
            if(moneyInATM < 0) throw new IllegalArgumentException();
            this.indexOfIllegalInput = 0;
            this.previousCard = null;
            this.currentCard = card;
            this.moneyInATM = moneyInATM;
	}

	public double getMoneyInATM() {
            return moneyInATM;
	}
        
        public int getIndexOfIllegalInput(){
            return indexOfIllegalInput; 
        }
        
   	 
	//С вызова данного метода начинается работа с картой
	//Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
	//Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
	public boolean validateCard(Card card, int pinCode){
           
            if(card.checkPin(pinCode) && !card.isBlocked()){
                indexOfIllegalInput = 0;
                currentCard = card;
                previousCard = card;
                return true;
            }

            else {
                if (card == previousCard){
                    
                    indexOfIllegalInput++;
                    if (indexOfIllegalInput == 3){
                        indexOfIllegalInput = 0;
                        card.block();
                    }
                    
                }else {
                    indexOfIllegalInput = 1;
                    previousCard = card;
                }
                
                return false;
            }
	}
    
	//Возвращает сколько денег есть на счету
	public double checkBalance() throws NoCardInserted{
            if (currentCard == null){
                throw new NoCardInserted();
            }
            else return currentCard.getAccount().getBalance();
	}
    
	//Метод для снятия указанной суммы
	//Метод возвращает сумму, которая у клиента осталась на счету после снятия
	//Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
	//Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount
	//Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM
	//При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
	public double getCash(double amount) throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted{
            if(amount > 0 && amount <= 4000)  {  
                if (currentCard != null){
                    if(checkBalance() - amount >= 0){
                        if(getMoneyInATM() - amount >= 0){
                            currentCard.getAccount().withdraw(amount);
                            this.moneyInATM = this.moneyInATM - amount;
                        }else throw new NotEnoughMoneyInATM();        
                    }else throw new NotEnoughMoneyInAccount();  
                }else throw new NoCardInserted();
            }else throw new IllegalArgumentException();
            return checkBalance();
	}

}
