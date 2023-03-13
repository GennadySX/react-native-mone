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

RCT_EXTERN_METHOD(filterImage:(NSString *)filterName
        withOptions:(NSDictionary *)options
        resolver:(RCTPromiseResolveBlock)resolve
        rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getFilters:(RCTPromiseResolveBlock)resolve rejected:(RCTPromiseRejectBlock)reject)


@end
