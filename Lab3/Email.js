import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    ListView,
    Button,
    Navigator,
    TouchableOpacity,
    TouchableHighlight,
    TextInput,
    Image,
    Alert,
} from 'react-native';

import Communications from 'react-native-communications';

export default class Email extends Component{
    constructor(props){
        super(props);
        this.state = {email: "vatca_razvan@yahoo.com" ,subject: "Email from your App", text: ""};
        this.goBack=()=>{
            this.props.navigator.push({id:1});
        };

        this.sendEmail=()=>{
            let email = this.state.email;
            let subject = this.state.subject;
            let text = this.state.text;

            if(email != "" && subject != "" && text != ""){
                Communications.email([email],null,null,subject,text)
            }else{
                Alert.alert('All fields must be filled!');
            }
        };
    }
        render() {
                return (
                    <View style={styles.container}>

                        <TextInput
                            style={styles.textInput}
                            onChangeText={(email) => this.setState({email})}
                            value={this.state.email}
                        />
                        <TextInput
                            style={styles.textInput}
                            onChangeText={(subject) => this.setState({subject})}
                            value={this.state.subject}
                        />
                        <TextInput
                            style={styles.textInput}
                            onChangeText={(text) => this.setState({text})}
                            value={this.state.text}
                        />

                        <TouchableOpacity onPress={this.sendEmail}>
                            <View
                                style={styles.button}>
                                <Text style={styles.textButton}>Send email</Text>
                            </View>
                        </TouchableOpacity>

                        <TouchableOpacity onPress={this.goBack}>
                            <View
                                style={styles.button}>
                                <Text style={styles.textButton}>Go BACK</Text>
                            </View>
                        </TouchableOpacity>
                    </View>
                );
            }

}

const styles = StyleSheet.create({
    button: {
        alignItems: 'center',
        width: 150, height: 50,
        backgroundColor: '#00BFFF',
        borderBottomWidth: 5,
        borderTopWidth: 10,
        margin: 10,
        borderRadius: 3
    },
    textInput:{
        color:'black',
        fontSize:20,
        width:200
    },
    container: {
        flex: 1,
        alignItems: 'center',
    },
    textButton: {
        fontSize: 20,
        textAlign: 'center',
        color: 'white',
    }
});