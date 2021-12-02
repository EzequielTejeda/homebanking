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
            client_cards: [],
            card_type: "",
            card_color: "",
            card_holder: "",
            message: "",
            errorMessage: false,
            creditCards: [],
            debitCards: []
        }
    },
    created(){
        axios.get("https://localhost:8080/api/clients/current")
        .then(response => {
            this.client_cards = response.data.cards
            this.card_holder = response.data.lastName + " " + response.data.firstName
        })
        .catch(error =>{
            console.log(error)
        })
    },
    methods:{
        createCard(){
            axios.post('https://localhost:8080/api/clients/current/cards',`type=${this.card_type}&color=${this.card_color}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => window.location.href = "https://localhost:8080/web/views/cards.html")
            .catch(error =>{
                console.log(error)
                this.filterCards("creditCards","CREDIT")
                this.filterCards("debitCards","DEBIT")
                if(this.card_type === ""){
                    this.message = "Choose a card type"
                    this.errorMessage = true
                    setTimeout(this.closeToast, 3000)
                }else if(this.card_color === ""){
                    this.message = "Choose a card color"
                    this.errorMessage = true
                    setTimeout(this.closeToast, 3000)
                }else if(this.card_type === "CREDIT" && this.creditCards.length >= 3){
                    this.message = "You have 3 credit cards already"
                    this.errorMessage = true
                    setTimeout(this.closeToast, 3000)
                }else if(this.card_type === "DEBIT" && this.debitCards.length >= 3){
                    this.message = "You have 3 debit cards already"
                    this.errorMessage = true
                    setTimeout(this.closeToast, 3000)
                }
                
            })
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "https://localhost:8080/web/index.html")
            .catch(error =>{
                console.log(error)
            })
        },
        closeToast(){
            this.errorMessage = false
        },
        filterCards(array,cardType){
            this[array] = this.client_cards.filter(card => card.type === cardType)
        }
    }
})
app.mount("#app")