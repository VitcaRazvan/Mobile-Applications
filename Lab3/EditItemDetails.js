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

export default class EditItemDetails extends Component{
    constructor(props){
        super(props);
        this.state = {text: this.props.can.toString()};
        this.goBack =()=>{
            this.props.navigator.push({id:2, cans: this.props.cans, changed:this.state.text, before: this.props.can.toString()});
        }
    }

    render(){
        return(
            <View style={styles.container}>
                <TextInput
                    style={{color:'black', fontSize:50,width:200,textAlign: 'center'}}
                    onChangeText={(text)=>this.setState({text})}
                    value={this.state.text}
                />
                <TouchableOpacity onPress={this.goBack}>
                    <View
                        style={styles.button}>
                        <Text style={styles.textButton}>Save</Text>
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
        paddingVertical: 10,
        paddingHorizontal: 20,
        backgroundColor: '#00BFFF',
        borderRadius: 3
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