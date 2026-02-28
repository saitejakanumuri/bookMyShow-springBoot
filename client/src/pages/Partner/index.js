import React from 'react';
import {Tabs} from 'antd';
import TheatresList from './TheatresList';

function Partner(){
    const tabItems = [
        {
            key: '1',
            label:'Theatres',
            children: <TheatresList/>,
        },
    ];

    return(
        <div>
            <h1>Partner Page</h1>
            <Tabs items = {tabItems} />
        </div>
    )
}

export default Partner;