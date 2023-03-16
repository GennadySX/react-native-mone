//
//  MoneFilter.m
//  Mone
//
//  Created by Gennady Sabirovsky on 13.03.2023.
//  Copyright © 2023 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"


@interface RCT_EXTERN_MODULE(MoneFilter, NSObject)

RCT_EXTERN_METHOD(filterImage:(NSString *)uri
            filter:(NSString *)filter
            callback:(RCTResponseSenderBlock)callback)



@end
