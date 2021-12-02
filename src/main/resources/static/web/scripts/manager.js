const app = Vue.createApp({
    data(){
        return{
            bank_clients: [],
            clientFirstName: "",
            clientLastName: "",
            clientEmail: "",
            clients_json: {}
        }
    },
    created(){
        this.getClients()

    },
    methods:{
        postClient() {
            axios.post("https://localhost:8080/rest/clients", {
                firstName: this.clientFirstName,
                lastName: this.clientLastName,
                email: this.clientEmail 
            })
            this.clientFirstName= "",
            this.clientLastName= "",
            this.clientEmail= "",
            this.getClients(),
            location.reload()
        },
        getClients(){
            axios.get("https://localhost:8080/rest/clients")
            .then(response => {
            this.bank_clients = response.data._embedded.clients
            this.clients_json = response.data
            })
            .catch(error =>{
                        console.log(error)
                    })
        }
    }
})
app.mount("#app")