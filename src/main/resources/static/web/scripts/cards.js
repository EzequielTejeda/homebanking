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
            client_cards: [],
            debit_cards: [],
            credit_cards: [],
            deleted_card: "",
            messageToast: false,
            message: "",
            expirationDate: ""
        }
    },
    created(){
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
            this.client_cards = response.data.cards
            this.client_cards.sort((a,b)=>{
                if (a.id < b.id){
                    return -1
                }else if (a.id > b.id){
                    return 1
                }
                return 0
            })
            this.verifyExpiration()
            this.filterByCardType()
        })
        .catch(error =>{
            console.log(error)
        })
    },
    methods:{
        filterByCardType(){
            for (let i=0; i < this.client_cards.length; i++){
                if (this.client_cards[i].type === "DEBIT" && this.client_cards[i].deleted === false){
                    this.debit_cards.push(this.client_cards[i])
                }else if(this.client_cards[i].type === "CREDIT" && this.client_cards[i].deleted === false){
                    this.credit_cards.push(this.client_cards[i])
                }
            }
        },
        deleteCardConfirmation(){
            axios.patch('/api/clients/current/cards/delete',`cardNumber=${this.deleted_card}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                this.deleted_card = ""
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
        cancelCardDelete(){
            this.deleted_card = ""
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "http://localhost:8080/web/index.html")
            .catch(error =>{
                console.log(error)
            })
        },
        changeCardPage(){
            window.location.href = "http://localhost:8080/web/views/create-cards.html"
        },
        verifyExpiration(){
            this.expirationDate = moment().format('MM/YY')
        }
    },
    computed:{
        deleteCard(){
            if(this.deleted_card != ""){
                return true
            }
        }
    }
})
app.mount("#app")