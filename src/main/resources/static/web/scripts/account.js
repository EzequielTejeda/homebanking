$(window).on('load', function () {
    setTimeout(function () {
        $(".loader-page").css({
            visibility: "hidden", opacity: "0"
        }, 2000)
    }, 300)
});

const app = Vue.createApp({


    data() {

        return {
            currentClientTransactions: [],
            currentClientAccounts: [],
            currentClientLoans: [],
            accountBalance: 0
        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current/account/transaction")
        .then(response => {
            this.currentClientAccounts = response.data
            this.lookForAccountTransactions()
            this.getAccountBalance()
        })
        .catch(error => {
            console.log(error)
        })
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
            this.currentClientLoans = response.data.loans
        })
        .catch(error => {
            console.log(error)
        })
    },
    methods: {
        signOut() {
            axios.post('/api/logout')
            .then(response => window.location.href = "http://localhost:8080/web/index.html")
            .catch(error => {
                console.log(error)
            })
        },
        lookForAccountTransactions(){
            let urlParams = new URLSearchParams(window.location.search)
            let myParam = urlParams.get('id')
            let myAccountsIds = []
            for(j = 0; j < this.currentClientAccounts.length; j++){
                myAccountsIds.push(this.currentClientAccounts[j].id)
            }
            if(myAccountsIds.includes(+myParam)){
                for(i = 0; i < this.currentClientAccounts.length; i++){
                    if((this.currentClientAccounts[i].id).toString() === myParam){
                        this.currentClientTransactions = this.currentClientAccounts[i].transactions
                    }
                }
            }else{
                window.location.href = "http://localhost:8080/web/views/accounts.html"
            }
        },
        getAccountBalance(){
            let urlParams = new URLSearchParams(window.location.search)
            let myParam = urlParams.get('id')
            this.accountBalance = (this.currentClientAccounts.find(account => (account.id).toString() === myParam)).balance
        }
    },
    computed: {
        
    }
})
app.mount("#app")