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

export default class ItemsList extends Component{
    constructor(props){
        super(props);
        var ds = new ListView.DataSource({rowHasChanged: (r1,r2) => r1 !== r2});
        this.state = {
           ds :[{Can: "Montana"},{Can: "Loop"},{Can: "Flame"},{Can: "Sabotaz80"},{Can: "Ironlak"}],
           dataSource: ds
        };
        this.goBack=()=>{this.props.navigator.push({id:1})};

        if (this.props.changed != null) {
            this.state.ds = this.props.cans;
            for(let i=0; i < this.props.cans.length; i++){
                if(this.state.ds[i].Can == this.props.before){
                    this.state.ds[i].Can = this.props.changed;
                    break;
                }
            }
        }
    }
    componentDidMount(){
        this.setState({
            dataSource: this.state.dataSource.cloneWithRows(this.state.ds)
        })
    };

    tapListRow(rowData){
        this.props.navigator.push({id:4,can: rowData.Can, cans: this.state.ds});
    };

    renderRow(rowData){
        return(
            <TouchableOpacity
                            onPress={()=> this.tapListRow(rowData)}
                            underlayColor='#ddd'>
                          <View style={styles.row}>
                            <Text style={{fontSize:18}}>{rowData.Can} </Text>
                          </View>
                        </TouchableOpacity>
        );
    }
    render(){
        return(
            <View style={styles.container}>
                <ListView dataSource={this.state.dataSource} renderRow={this.renderRow.bind(this)}/>

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
    container: {
        flex: 1,
        alignItems: 'center',
    },
    textButton: {
        fontSize: 20,
        textAlign: 'center',
        color: 'white',
    },
    text: {
        fontSize: 18,
        color:'black'
    },
    row: {
        flex: 1,
        flexDirection: 'row',
        padding: 18,
        borderBottomWidth: 1,
        borderColor: '#d7d7d7',
    }
})
