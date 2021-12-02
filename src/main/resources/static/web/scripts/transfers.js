$(window).on('load', function () {
    setTimeout(function(){
        $(".loader-page").css({
            visibility: "hidden", opacity: "0"
        }, 2000)
    }, 300)
});
const app = Vue.createApp({
    data(){
        return{
            accounts: [],
            accountContacts: [],
            originAccount: "",
            destinationAccount: "",
            transactionAmount: 0,
            transactionDescription: "",
            contact: "CANCEL",
            contactToast: false,
            contactTypeToast: false,
            destinationAccountType: "",
            transactionToast: false,
            messageToast: false,
            message: ""
        }
    },
    created(){
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
            this.accounts = response.data.accounts.filter(account => account.deleted === false)
            this.accounts.sort((a,b)=>{
                if (a.id < b.id){
                    return -1
                }
                else if (a.id > b.id){
                    return 1
                }
                return 0
            })
        })
    },
    methods:{
        submitTransaction(){
            let ownAccountNumbers = []
            for(i = 0; i < this.accounts.length; i++){
                ownAccountNumbers.push(this.accounts[i].number)
            }
            if(this.originAccount === "" || this.destinationAccount === "" || this.transactionDescription === ""){
                this.messageToast = true
                this.message = "Fill in every field first"
                setTimeout(this.closeMessageToast, 3000)
            }
            else if(this.originAccount === this.destinationAccount){
                this.messageToast = true
                this.message = "Destination account and origin account can not be the same"
                setTimeout(this.closeMessageToast, 3000)
            }
            else if(ownAccountNumbers.includes(this.destinationAccount) && this.destinationAccountType === "otherAccount"){
                this.messageToast = true
                this.message = "It is not possible transfer money to an account you own if 'Other Account' field was chosen"
                setTimeout(this.closeMessageToast, 3000)
            }
            else if(this.transactionAmount < 1){
                this.messageToast = true
                this.message = "Amount can not be less than 1"
                setTimeout(this.closeMessageToast, 3000)
            }
            else if(this.transactionAmount > this.returnAmountMoney){
                this.messageToast = true
                this.message = "There is not enough money in the account"
                setTimeout(this.closeMessageToast, 3000)
            }
            else{
                this.messageToast = false
                if(this.destinationAccountType === "otherAccount"){
                    let accountContactNumbers = []
                    for(i = 0; i < this.accounts.length; i++){
                        if(this.accounts[i].number === this.originAccount){
                            for(j = 0; j < this.accounts[i].accountContacts.length; j++){
                                accountContactNumbers.push(this.accounts[i].accountContacts[j].accountNumber)
                            }
                        }
                    }
                    if(accountContactNumbers.includes(this.destinationAccount[0]) || accountContactNumbers.includes(this.destinationAccount)){
                        this.transactionToast = true
                    }else{
                        this.contactToast = true
                    }
                }else{
                this.transactionToast = true
                }
            }
        },
        makeTransaction(){
            axios.post('http://localhost:8080/api/clients/current/transactions',`amount=${this.transactionAmount}&description=${this.transactionDescription}&originAccount=${this.originAccount}&destinationAccount=${this.destinationAccount}&saveContact=${this.contact}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response =>{
                this.transactionToast = false
                this.message = "Transaction was successful!"
                this.messageToast = true
                this.originAccount = ""
                this.destinationAccount = ""
                this.destinationAccountType = ""
                this.transactionAmount = 0
                this.transactionDescription = ""
                this.contact= "CANCEL"
                this.accountContacts = []
                setTimeout(function(){
                    location.reload()
                }, 1000)
            })
            .catch(error =>{
                this.transactionToast = false
                this.message = error.response.data
                this.messageToast = true
                this.contact= "CANCEL"
            })
        },
        cancelTransaction(){
            this.transactionToast = false
        },
        saveContact(){
            this.contactToast = false
            this.contactTypeToast = true
        },
        notSaveContact(){
            this.contactToast = false
            this.transactionToast = true
        },
        saveContactType(){
            if(this.contact === "CANCEL"){
                this.message = "You must choose one option to save the contact"
            }else{
                this.contactTypeToast = false
                this.transactionToast = true
            }
        },
        notSaveContactType(){
            this.contactTypeToast = false
            this.contactToast = true
            this.contact = "CANCEL"
            this.message = ""
        },
        closeMessageToast(){
            this.messageToast = false
        },
        signOut(){
            axios.post('http://localhost:8080/api/logout')
            .then(response => window.location.href = "http://localhost:8080/web/index.html")
            .catch(error =>{
                console.log(error)
            })
        }
    },
    computed:{
        returnAmountMoney(){
            let choosenAccount = this.accounts.find(account => account.number === this.originAccount)
            return choosenAccount.balance
        },
        returnAccountContacts(){
            this.accountContacts = []
            if(this.originAccount != ""){
                for(i = 0; i < this.accounts.length; i++){
                    if(this.accounts[i].number === this.originAccount){
                        this.accountContacts = this.accounts[i].accountContacts
                    }
                }
            }
            return this.accountContacts
        }
    }

})
app.mount("#app")