$(window).on('load', function () {
    setTimeout(function(){
        $(".loader-page").css({
            visibility: "hidden", opacity: "0"
        }, 2000)
    }, 300)
});
const app = Vue.createApp({
    data() {
        return {
            emailLogin: "",
            passwordLogin: "",
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            signUp: false,
            accountsLength: 0,
            message: "",
            loginError: false,
            signUpError: false,
            accountType: "CURRENT"
        }
    },
    created(){

    },
    methods: {
        verifyUser(){
            axios.post('https://localhost:8080/api/login',`email=${this.email}&password=${this.password}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => window.location.href = "https://localhost:8080/web/views/accounts.html")
            .catch(error =>{
                this.message = "Invalid email or password"
                this.loginError = true
                setTimeout(this.closeLoginToast, 3000)
            })
        },
        createUser(){
            axios.post('https://localhost:8080/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => axios.post('https://localhost:8080/api/login',`email=${this.email}&password=${this.password}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}}))
            .then(response => axios.post('https://localhost:8080/api/clients/current/accounts', `accountType=${this.accountType}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}}))
            .then(response =>  window.location.href = "https://localhost:8080/web/views/accounts.html")
            .catch(error =>{
                console.log(error)
                if(this.firstName === "" || this.lastName === "" || this.email === "" || this.password === ""){
                    this.message = "Missing data"
                    this.signUpError = true
                    setTimeout(this.closeSignUpToast, 3000)
                }
                else{
                    this.message = "Email already in use"
                    this.signUpError = true
                    setTimeout(this.closeSignUpToast, 3000)
                }
            })
        },
        signUpTrue(){
            this.signUp = true
        },
        signUpFalse(){
            this.signUp = false
        },
        closeLoginToast(){
            this.loginError = false
        },
        closeSignUpToast(){
            this.signUpError = false
        }
    }
})
app.mount("#app")