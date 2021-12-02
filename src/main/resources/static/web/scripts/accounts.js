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
            client_data: [],
            accounts_data: [],
            loans:[],
            deleted_account: "",
            chosenAccountType: "",
            accountTypeConfirmation: false,
            message: "",
            messageToast: false
        }
    },
    created(){
        axios.get("https://localhost:8080/api/clients/current")
        .then(response => {
            this.client_data = response.data
            this.loans = this.client_data.loans
            this.accounts_data = this.client_data.accounts.filter(account => account.deleted === false)
            this.accounts_data.sort((a,b)=>{
                if (a.id < b.id){
                    return -1
                }
                else if (a.id > b.id){
                    return 1
                }
                return 0
            })
            this.email = this.client_data.email
            this.accountsLength = this.accounts_data.length
        })
        .catch(error =>{
            console.log(error)
        })
    },
    methods: {
        signOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "https://localhost:8080/web/index.html")
            .catch(error =>{
                console.log(error)
            })

        },
        getAccountType(){
            this.accountTypeConfirmation = true
        },
        createAccount(){
            axios.post('https://localhost:8080/api/clients/current/accounts', `accountType=${this.chosenAccountType}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => location.reload())
        },
        cancelAccountCreation(){
            this.accountTypeConfirmation = false
        },
        deleteAccountConfirmation(){
            axios.patch('https://localhost:8080/api/clients/current/accounts/delete',`accountNumber=${this.deleted_account}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                this.deleted_account = ""
                this.message = response.data
                this.messageToast = true
                setTimeout(function(){
                    location.reload()
                }, 1000)
            })
            .catch(error => {
                console.log(error.response.data)
            })
        },
        cancelAccountDelete(){
            this.deleted_account = ""
        },
        closeMessageToast(){
            this.messageToast = false
        },
        loanApply(){
            window.location.href = "https://localhost:8080/web/views/loan-application.html"
        }
    },
    computed:{
        deleteAccount(){
            if(this.deleted_account != ""){
                let accountToDelete = this.accounts_data.find(account => account.number === this.deleted_account)
                if(accountToDelete.balance > 0){
                    this.message = "Make sure to transfer the balance to another account before delete it"
                    this.messageToast = true
                    setTimeout(this.closeMessageToast, 3000)
                    setTimeout(this.cancelAccountDelete, 3000)
                    return false
                }else if(this.accounts_data.length < 2){
                    this.message = "Client should have at least one account"
                    this.messageToast = true
                    setTimeout(this.closeMessageToast, 3000)
                    setTimeout(this.cancelAccountDelete, 3000)
                    return false
                }else{
                    return true
                }
            }
        }
    }
})
app.mount("#app")