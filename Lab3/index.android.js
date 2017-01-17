/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    ListView,
    Button,
    Navigator,
    TouchableHighlight,
    TouchableOpacity,
    TextInput,
    Image,
    Alert,
} from 'react-native';

import ItemsList from './ItemsList.js';
import EditItemDetails from './EditItemDetails.js'
import Email from './Email.js'

var FirstPage = React.createClass({

    listButtonPress(){
        this.props.navigator.push({id:2});
    },
    emailButtonPress(){
        this.props.navigator.push({id:3});
    },

    render(){
        return(
            <View style={[styles.container,{backgroundColor:'white'}]}>
                <Text style={styles.whatYouWant}>What do you want to do?</Text>
                <TouchableOpacity onPress={this.listButtonPress}>
                    <View
                        style={styles.button}>
                        <Text style={styles.textButton}> List of Cans</Text>
                    </View>
                </TouchableOpacity>
                <TouchableOpacity onPress={this.emailButtonPress}>
                   <View
                        style={styles.button}>
                        <Text style={styles.textButton}> Send Feedback!</Text>
                   </View>
                </TouchableOpacity>
                <Image source={require('./spraycanvector-675694.jpg')} style={styles.image}/>
            </View>
        )
    },
});

class ReactNativeNavigation extends Component{
    _renderScene(route,navigator){
        if(route.id === 1){
            return <FirstPage navigator={navigator}/>
        }else if(route.id === 2){
            return <ItemsList navigator={navigator} changed={route.changed} before={route.before} cans={route.cans}/>
        }else if(route.id === 3){
            return <Email navigator={navigator}/>
        }else if(route.id ===4){
            return <EditItemDetails navigator={navigator} can={route.can} cans={route.cans} />
        }
    }

    render(){
        return(
            <Navigator  initialRoute={{id:1,}} renderScene={this._renderScene}/>
        );
    }
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
    },
    button: {
        alignItems: 'center',
        width: 160, height: 50,
        backgroundColor: '#00BFFF',
        borderTopWidth: 10,
        borderBottomWidth: 5,
        margin: 10,
        borderRadius: 3
    },
    textButton: {
        fontSize: 20,
        textAlign: 'center',
        color: 'white',
    },
    whatYouWant: {
        margin: 10,
        fontSize: 30,
        color: "black"
    },
    image :{
        marginLeft: 100,
        marginTop: 10,
        width: 360,
        height: 360
    }
});


AppRegistry.registerComponent('Lab3', () => ReactNativeNavigation);
