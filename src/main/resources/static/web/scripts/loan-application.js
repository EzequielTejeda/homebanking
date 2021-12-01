$(window).on('load', function () {
    setTimeout(function(){
        $(".loader-page").css({
            visibility: "hidden", opacity: "0"
        }, 2000)
    }, 300)
});
const app = Vue.createApp({
    data(){
        return {
            loans: [],
            clientAccountsNumbers: [],
            loanType: "",
            payments: "",
            loanAmount: 0,
            destinationAccount: "",
            clientLoanTypes: [],
            loanToast: false,
            messageToast: false,
            message: ""
        }
    },
    created(){
        axios.get("http://localhost:8080/api/loans")
        .then(response => {
            this.loans = response.data
        })
        .catch(error => {
            console.log(error)
        })
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
            let filtredAccounts = response.data.accounts.filter(account => account.deleted === false)
            this.lookForAccountNumbers(filtredAccounts)
            this.getClientLoanTypes(response.data.loans)
        })
        .catch(error => {
            console.log(error)
        })
    },
    methods: {
        signOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "http://localhost:8080/web/index.html")
            .catch(error =>{
                console.log(error)
            })
        },
        lookForAccountNumbers(array){
            for(i = 0; i < array.length; i++){
                this.clientAccountsNumbers.push(array[i].number)
            }
            this.clientAccountsNumbers.sort((a,b)=>{
                if (a < b){
                    return -1
                }
                else if (a > b){
                    return 1
                }
                return 0
            })
        },
        getClientLoanTypes(array){
            for(i = 0; i < array.length; i++){
                this.clientLoanTypes.push(array[i].name)
            }
        },
        verifyLoan(){
            if(this.loanType === "" || this.payments === "" || this.destinationAccount === ""){
                this.message = "Fill in every field first"
                this.messageToast = true
                setTimeout(this.closeToast, 3000)
            }
            else if(this.clientLoanTypes.includes(this.loanType)){
                this.message = "It is not possible apply for a loan you got already"
                this.messageToast = true
                setTimeout(this.closeToast, 3000)
            }
            else if(this.loanAmount > this.returnMaxAmountLoan || this.loanAmount <= 0){
                this.message = "Invalid amount"
                this.messageToast = true
                setTimeout(this.closeToast, 3000)
            }
            else{
                this.messageToast = false
                this.loanToast = true
                setTimeout(this.closeToast, 3000)
            }
        },
        closeToast(){
            this.messageToast = false
        },
        cancelLoanRequest(){
            this.loanToast = false
        },
        requestLoan(){
            axios.post('/api/clients/current/loans',{ id: `${this.findLoanId}`, amount: `${this.loanAmount}`, payments: `${this.payments}`, destinationAccount: `${this.destinationAccount}`})
            .then(response => {
                this.loanToast = false
                this.message = response.data
                this.messageToast = true
                setTimeout(function(){
                    location.reload()
                }, 1000)
            })
            .catch(error => 
                console.log(error.response.data)
            )
        }
    },
    computed: {
        getPayments(){
            this.payments = ""
            let loanPayments = []
            if(this.loanType != ""){
                loanPayments = (this.loans.find(loan => loan.name === this.loanType)).payments
            }
            return loanPayments
        },
        returnMaxAmountLoan(){
            let maxAmountLoan = 0
            if(this.loanType != ""){
                maxAmountLoan = (this.loans.find(loan => loan.name === this.loanType)).maxAmount
            }
            return maxAmountLoan
        },
        totalToPay(){
            let interestImp = this.loanAmount * (this.loanInterest / 100)
            let total = this.loanAmount + interestImp
            return total
        },
        loanInterest(){
            let selectedLoan = this.loans.find(loan => loan.name === this.loanType)
            let loanInterest = selectedLoan.interest
            return loanInterest
        },
        amountToPay(){
            let eachPaymentAmount = (this.totalToPay / (+this.payments)).toFixed(2)
            return eachPaymentAmount
        },
        findLoanId(){
            let loanId = 0
            if(this.loanType != ""){
                loanId = (this.loans.find(loan => loan.name === this.loanType)).id
            }
            return loanId
        },
        verifyFields(){
            let allFieldsComplete = false
            if(this.loanType != "" && this.payments != "" && this.loanAmount > 0 && this.returnMaxAmountLoan >= this.loanAmount && this.destinationAccount != ""){
                allFieldsComplete = true
            }
            return allFieldsComplete
        }
    }
})
app.mount("#app")